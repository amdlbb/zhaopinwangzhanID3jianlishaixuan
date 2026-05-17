package com.abc.xyzp.service.impl;

import cn.hutool.json.JSONUtil;
import com.abc.xyzp.common.MyConstants;
import com.abc.xyzp.common.exceptor.MyException;
import com.abc.xyzp.entity.Result;
import com.abc.xyzp.entity.Team;
import com.abc.xyzp.entity.User;
import com.abc.xyzp.mapper.TeamMapper;
import com.abc.xyzp.mapper.UserMapper;
import com.abc.xyzp.service.EmailService;
import com.abc.xyzp.utils.ThreadPoolMonitorUtils;
import com.abc.xyzp.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private UserMapper userMapper;

    @Resource
    private TeamMapper teamMapper;

    @Resource(name = "mailTaskExecutor")
    private Executor mailTaskExecutor;


    /*JavaMail API (javax.mail / jakarta.mail):
    Java 官方提供的邮件处理标准 API。Spring Boot Mail 底层依赖于此。它负责构建 MIME 消息（支持文本、HTML、附件等）。*/
    public void sendEmail(String title, String content, String from, String to){
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            // 3. 设置发件人（格式：邮箱(显示名称)）
            mimeMessageHelper.setFrom(MyConstants.FROM + "(" + from + ")");

            // 4. 设置收件人
            mimeMessageHelper.setTo(to);

            // 5. 设置邮件主题
            mimeMessageHelper.setSubject(title);

            //  true表示支持html格式
            mimeMessageHelper.setText(content, true);

            log.info("sendemail发送邮件");

            // 发送邮件
            javaMailSender.send(mimeMessage);

            log.info("邮件已发送");
        } catch (Exception e){
            e.printStackTrace();
            throw new MyException("发送邮箱出错，请正确输入" + e.getMessage());
        }
    }

    /**
     * 发送验证码邮件
     * @param uuid
     * @param email
     * @return
     */
    @Override
    public Result<String> send(String uuid, String email) {
        try {
            log.info("send方法被调用");
            // 4位随机数
            Random random = new Random();
            String number = random.nextInt(9999-1000+1)+1000 + "";
            // 存放redis, 并且设置过期时间为3分钟
            redisTemplate.opsForValue().set(MyConstants.REGISTER_CODE + uuid, number, 3, TimeUnit.MINUTES);
            log.info("开始发送给用户email={}，验证码：{}", email, number);


            // 异步执行发送
            final String finalNumber = number;
            mailTaskExecutor.execute(() -> {
                ThreadPoolMonitorUtils.printPoolStatus((org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor)
                        mailTaskExecutor, "发送验证码前");
                try {
                    sendEmail(MyConstants.TITLE, MyConstants.CONTENT.replace("6666", finalNumber), MyConstants.FROM + "校园招聘", email);
                } catch (Exception e) {
                    log.error("异步发送验证码邮件失败: {}", e.getMessage());
                }
                ThreadPoolMonitorUtils.printPoolStatus((org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor)
                        mailTaskExecutor, "发送验证码后");
            });


            log.info("邮件发送任务已提交给多线程任务处理");
            return Result.success("发送邮件成功");
        } catch (Exception e) {
            log.error("邮件发送异常", e);
            throw new RuntimeException("邮件发送失败", e);
        }
    }

    @Override
    public Result<String> sendInterview(HttpServletRequest httpServletRequest, Map<String, Object> data) {
        // 用发邮件的形式通知用户
        Long teamId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        Team team = JSONUtil.toBean((String) redisTemplate.opsForValue().get(MyConstants.USERINFO + teamId), Team.class);
        Long userId = Long.valueOf(String.valueOf(data.get("userId")));
        String address = String.valueOf(data.get("address"));
        LocalDateTime beginTime = TimeUtils.stampToLocalDateTime(Long.valueOf(String.valueOf(data.get("beginTime"))));
        LocalDateTime endTime = TimeUtils.stampToLocalDateTime(Long.valueOf(String.valueOf(data.get("endTime"))));
        // 查询用户
        User user = userMapper.queryUserById(userId);
        String title = MyConstants.INTERVIEW_TITLE + user.getNickname();
        String content = MyConstants.INTERVIEW_CONTENT.replace("6666", beginTime.toString()).replace("7777", endTime.toString())
                .replace("8888", address).replace("5555", String.valueOf(data.get("jobName")));


        // 异步执行
        mailTaskExecutor.execute(() -> {
            ThreadPoolMonitorUtils.printPoolStatus((org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor)
                    mailTaskExecutor, "发送面试通知前");
            try {
                sendEmail(title, content, team.getNickname(), user.getEmail());
            } catch (Exception e) {
                log.error("异步发送面试通知邮件失败: {}", e.getMessage());
            }
            ThreadPoolMonitorUtils.printPoolStatus((org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor)
                    mailTaskExecutor, "发送面试通知后");
        });

        return Result.success("发送邮件成功");
    }

    @Override
    public Result<String> sendAdmission(HttpServletRequest httpServletRequest, Map<String, Object> data) {
        // 用发邮件的形式通知用户
        Long teamId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        Team team = JSONUtil.toBean((String) redisTemplate.opsForValue().get(MyConstants.USERINFO + teamId), Team.class);
        Long userId = Long.valueOf(String.valueOf(data.get("userId")));
        String address = String.valueOf(data.get("workAddress"));
        LocalDateTime workTime = TimeUtils.stampToLocalDateTime(Long.valueOf(String.valueOf(data.get("workTime"))));
        // 查询用户
        User user = userMapper.queryUserById(userId);
        String title = MyConstants.INTERVIEW_TITLE + user.getNickname();
        String content = MyConstants.ADMISSION_CONTENT.replace("7777", workTime.toString())
                .replace("8888", address).replace("5555", String.valueOf(data.get("jobName")));


        // 异步执行
        mailTaskExecutor.execute(() -> {
            ThreadPoolMonitorUtils.printPoolStatus((org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor)
                    mailTaskExecutor, "发送录取通知前");
            try {
                sendEmail(title, content, team.getNickname(), user.getEmail());
            } catch (Exception e) {
                log.error("异步发送录取通知邮件失败: {}", e.getMessage());
            }
            ThreadPoolMonitorUtils.printPoolStatus((org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor)
                    mailTaskExecutor, "发送录取通知后");
        });

        return Result.success("发送邮件成功");
    }


    @Override
    public Result<String> sendJobOfflineNotification(Long teamId, String jobName) {
        try {
            // 1. 获取团队信息 (建议直接查库，确保数据最新且不为空，也可以尝试从Redis获取)
            Team team = teamMapper.queryUserById(teamId);
            if (team == null || team.getEmail() == null) {
                log.warn("团队ID: {} 不存在或无邮箱，无法发送下架通知", teamId);
                return Result.error("团队信息缺失");
            }

            // 2. 构建邮件内容
            String title = MyConstants.JOB_OFFLINE_TITLE;
            String content = MyConstants.JOB_OFFLINE_CONTENT.replace("${jobName}", jobName);


            // 3. 发送邮件
            // from 显示为 "系统通知" 或类似名称
            mailTaskExecutor.execute(() -> {
                ThreadPoolMonitorUtils.printPoolStatus((org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor)
                        mailTaskExecutor, "发送下架通知前");
                try {
                    sendEmail(title, content, "系统通知", team.getEmail());
                    log.info("职位下架通知邮件已发送给团队: {}, 职位: {}", team.getNickname(), jobName);
                } catch (Exception e) {
                    log.error("异步发送职位下架通知邮件异常", e);
                }
                ThreadPoolMonitorUtils.printPoolStatus((org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor)
                        mailTaskExecutor, "发送下架通知后");
            });


            return Result.success("发送下架通知成功");
        } catch (Exception e) {
            log.error("发送职位下架通知邮件异常", e);
            // 这里不抛出异常，避免影响主业务流程，仅记录日志
            return Result.error("发送下架通知失败: " + e.getMessage());
        }
    }

    @Override
    public Result<String> sendOnboardEmail(String title, String content, String toEmail) {
        try {
            log.info("开始发送入职邮件给: {}", toEmail);

            mailTaskExecutor.execute(() -> {
                ThreadPoolMonitorUtils.printPoolStatus((org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor)
                        mailTaskExecutor, "发送入职邮件前");
                try {
                    sendEmail(title, content, "校园招聘", toEmail);
                    log.info("入职邮件已发送给: {}", toEmail);
                } catch (Exception e) {
                    log.error("异步发送入职邮件失败: {}", e.getMessage());
                }
                ThreadPoolMonitorUtils.printPoolStatus((org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor)
                        mailTaskExecutor, "发送入职邮件后");
            });

            return Result.success("发送邮件成功");
        } catch (Exception e) {
            log.error("发送入职邮件异常", e);
            return Result.error("发送邮件失败: " + e.getMessage());
        }
    }


}
