package com.abc.xyzp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.abc.xyzp.common.MyConstants;
import com.abc.xyzp.common.SnowFlake;
import com.abc.xyzp.common.exceptor.MyException;
import com.abc.xyzp.dto.TeamDto;
import com.abc.xyzp.entity.*;
import com.abc.xyzp.mapper.*;
import com.abc.xyzp.service.EmailService;
import com.abc.xyzp.service.TeamService;
import com.abc.xyzp.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TeamServiceImpl implements TeamService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private TeamMapper teamMapper;

    @Resource
    private ExamineMapper examineMapper;

    @Resource
    private InterViewMapper interViewMapper;

    @Resource
    private DeliverMapper deliverMapper;

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private UserMapper userMapper;

    @Resource
    private AdmissionMapper admissionMapper;

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private EmailService emailService;


    @Override
    public Result<TeamDto> getTeamInfo(HttpServletRequest httpServletRequest) {
        TeamDto teamDto;
        String id = String.valueOf(httpServletRequest.getAttribute("userId"));
        String teamStr = (String) redisTemplate.opsForValue().get(MyConstants.TEAMINFO + id);
        // 先看redis中是否有数据
        if(teamStr == null){
            teamDto = BeanUtil.copyProperties(teamMapper.queryUserById(Long.valueOf(id)), TeamDto.class);
            redisTemplate.opsForValue().set(MyConstants.TEAMINFO + id, JSONUtil.toJsonStr(teamDto), 1, TimeUnit.HOURS);
        }
        else{
            teamDto = JSONUtil.toBean(teamStr, TeamDto.class);
        }
        return Result.success(teamDto);
    }


    @Override
    public Result<TeamDto> getTeamInfo2(HttpServletRequest httpServletRequest, Long id) {
        TeamDto teamDto;
        String teamStr = (String) redisTemplate.opsForValue().get(MyConstants.TEAMINFO + id);
        if(teamStr == null){
            Team user1 = teamMapper.queryUserById(Long.valueOf(id));
            // 检查用户是否存在
            if(user1 == null){
                throw new MyException("用户不存在");
            }
            // 去掉敏感信息
            teamDto = BeanUtil.copyProperties(user1, TeamDto.class);
            redisTemplate.opsForValue().set(MyConstants.TEAMINFO + id, JSONUtil.toJsonStr(teamDto), 1, TimeUnit.HOURS);
        }
        else{
            teamDto = JSONUtil.toBean(teamStr, TeamDto.class);
        }

        return Result.success(teamDto);
    }

    @Override
    @Transactional
    public Result<String> updateTeamInfo(Team team, HttpServletRequest httpServletRequest) {
        // 验证身份
        /*Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        if(!userId.equals(team.getId())){
            throw new MyException("身份验证失败");
        }*/
        // 保存修改后的结果
        if(teamMapper.updateById(team) <= 0){
            throw new MyException("更新出错");
        }
        // 更新reids
        TeamDto teamDto = BeanUtil.copyProperties(team, TeamDto.class);
        redisTemplate.opsForValue().set(MyConstants.TEAMINFO + team.getId(), JSONUtil.toJsonStr(teamDto), 1, TimeUnit.HOURS);
        return Result.success("更新成功");
    }

    @Override
    public Result<String> updateTeamPassword(PasswordForm passwordForm, HttpServletRequest httpServletRequest) {
        Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 检验密码是否正确
        if(!DigestUtil.md5Hex(passwordForm.getBeforePassword()).equals(teamMapper.queryUserById(userId).getPassword())){
            throw new MyException("密码不正确");
        }
        // 更新密码
        if(teamMapper.updatePasswordById(userId, DigestUtil.md5Hex(passwordForm.getNowPassword())) <= 0){
            throw new MyException("更新密码出错");
        }
        return Result.success("更新密码成功");
    }

    @Override
    @Transactional
    public Result<String> saveTeamJob(HttpServletRequest httpServletRequest, TeamJob teamJob) {
        Long teamId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 保存团队招聘信息
        teamJob.setTeamId(teamId);
        Long id = SnowFlake.nextId();
        teamJob.setId(id);
        teamJob.setStatus(1);
        if(teamMapper.saveTeamJob(teamJob) <= 0){
            throw new MyException("保存团队招聘信息出错");
        }
        // 保存审核信息
        Examine examine = new Examine();
        examine.setTeamJobId(id);
        examine.setId(SnowFlake.nextId());
        examine.setResult(0);
        if(examineMapper.save(examine) <= 0){
            throw new MyException("保存审核信息出错");
        }

        return Result.success("发布成功，等待管理员审核");
    }

    @Override
    public Result<Map<String, Object>> getTeamJob(HttpServletRequest httpServletRequest, int page, int pageSize) {
        Long teamId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        Map<String, Object> res = new HashMap<>();
        res.put("data", teamMapper.queryTeamJobListById(teamId, (page - 1) * pageSize, pageSize));
        res.put("totalNum", teamMapper.queryTeamJobTotalById(teamId));
        return Result.success(res);
    }

    @Override
    public Result<Map<String, Object>> searchTeamJob(HttpServletRequest httpServletRequest, String content) {
        Long teamId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        log.info(String.valueOf(teamId));
        Map<String, Object> res = new HashMap<>();
        res.put("data", teamMapper.searchTeamJobListById(teamId, content));
        res.put("totalNum", teamMapper.searchTeamJobTotalById(teamId, content));
        return Result.success(res);
    }

    @Override
    public Result<Map<String, Object>> getNewTeamJob(int page, int pageSize) {
        Map<String, Object> res = new HashMap();
        res.put("list", teamMapper.queryNewTeamJob((page - 1) * pageSize, pageSize));
        res.put("totalNum", teamMapper.queryNewTeamJobTotal());
        return Result.success(res);
    }

    @Override
    public Result<TeamJob> getTeamJobInfo(Long id) {
        TeamJob teamJob = teamMapper.queryTeamJobById(id);
        if(ObjectUtil.isNull(teamJob)){
            return Result.error("该招聘信息不存在或者已经下架");
        }
        return Result.success(teamMapper.queryTeamJobById(id));
    }

    @Override
    public Result<String> updateTeamJob(HttpServletRequest httpServletRequest, TeamJob teamJob) {
        Long teamId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 验证身份
        if(teamMapper.queryCountTeamJobNum(teamId, teamJob.getId()) != 1){
            throw new MyException("身份验证失败");
        }
        // 更新
        if(teamMapper.updateTeamJob(teamJob) <= 0){
            throw new MyException("更新团队信息出错");
        }

        return Result.success("更新成功");
    }


    @Override
    public Result<Map<String, Object>> getUserResume(HttpServletRequest httpServletRequest, int page, int pageSize) {
        Long teamId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        Map<String, Object> res = new HashMap<>();
        res.put("data", teamMapper.queryUserResumeById(teamId, (page - 1) * pageSize, pageSize));
        res.put("totalNum", teamMapper.queryUserResumeTotalById(teamId));
        return Result.success(res);
    }

    @Override
    public Result<Map<String, Object>> searchUserResume(HttpServletRequest httpServletRequest, String content) {
        Long teamId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        Map<String, Object> res = new HashMap<>();
        res.put("data", teamMapper.searchUserResumeById(teamId, content));
        res.put("totalNum", teamMapper.searchUserResumeTotalById(teamId, content));
        return Result.success(res);
    }

    @Override
    public Result<String> updateTeamJobStatus(HttpServletRequest httpServletRequest, Long id, Long status) {
        Long teamId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        if(teamMapper.updateTeamJobStatus(teamId, status, id) <= 0){
            throw new MyException("更新状态出错");
        }
        return Result.success("更新状态成功");
    }

    @Transactional
    @Override
    public Result<String> saveInterView(HttpServletRequest httpServletRequest, Map<String, Object> data) {
        Long teamJobId = Long.valueOf(String.valueOf(data.get("teamJobId")));
        Long userId = Long.valueOf(String.valueOf(data.get("userId")));
        LocalDateTime beginTime = TimeUtils.stampToLocalDateTime(Long.valueOf(String.valueOf(data.get("beginTime"))));
        LocalDateTime endTime = TimeUtils.stampToLocalDateTime(Long.valueOf(String.valueOf(data.get("endTime"))));
        Interview interView = new Interview();
        interView.setId(SnowFlake.nextId());
        interView.setTeamJobId(teamJobId);
        interView.setUserId(userId);
        interView.setAddress(String.valueOf(data.get("address")));
        interView.setEndTime(beginTime);
        interView.setBeginTime(endTime);
        if(interViewMapper.save(interView) <= 0){
            throw new MyException("保存面试出错");
        }
        // 更改投递状态
        Long deliverId = Long.valueOf(String.valueOf(data.get("deliverId")));
        if(deliverMapper.updateUserDeliverStatusById(userId, deliverId, 2) <= 0){
            throw new MyException("更新投递状态出错");
        }
        return Result.success("保存面试成功");
    }


    @Override
    public Result<String> updateDeliverStatus(HttpServletRequest httpServletRequest, Map<String, Object> data) {
        Long userId = Long.valueOf(String.valueOf(data.get("userId")));
        Long deliverId = Long.valueOf(String.valueOf(data.get("deliverId")));
        if(deliverMapper.updateUserDeliverStatusById(userId, deliverId, 3) <= 0){
            throw new MyException("更新投递状态出错");
        }
        return Result.success("更新投递状态成功");
    }


    @Override
    public Result<Map<String, Object>> getInterviewInfo(HttpServletRequest httpServletRequest, int page, int pageSize) {
        Long teamId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        Map<String, Object> res = new HashMap<>();
        res.put("data", teamMapper.queryInterviewById(teamId, (page - 1) * pageSize, pageSize));
        res.put("totalNum", teamMapper.queryInterviewTotalById(teamId));
        return Result.success(res);
    }

    /**
     * 录取方法
     * */
    @Transactional
    @Override
    public Result<String> admissionUser(HttpServletRequest httpServletRequest, Map<String, Object> data) {
        Long teamJobId = Long.valueOf(String.valueOf(data.get("teamJobId")));

        Admission admission = new Admission();
        admission.setId(SnowFlake.nextId());
        admission.setUserId(Long.valueOf(String.valueOf(data.get("userId"))));
        admission.setTeamJobId(Long.valueOf(String.valueOf(data.get("teamJobId"))));
        admission.setWorkAddress(String.valueOf(data.get("workAddress")));
        admission.setWorkTime(TimeUtils.stampToLocalDateTime(Long.valueOf(String.valueOf(data.get("workTime")))));
        // 保存录取信息
        if(admissionMapper.save(admission) <= 0){
            return Result.error("保存录取出错");
        }
        // 更改面试状态
        Long userId = Long.valueOf(String.valueOf(data.get("userId")));
        Long interviewId = Long.valueOf(String.valueOf(data.get("interviewId")));
        if(interViewMapper.updateInterviewStatusById(userId, interviewId, 3) <= 0){
            return Result.error("更新面试状态出错");
        }

        // 4. 检查剩余数量，如果为 0，则自动下架职位 (status = 5)
        Integer remainingNumber = teamMapper.getJobNumberById(teamJobId);
        log.info("剩余数量：{}", remainingNumber);

        // 3. 扣减职位数量
        // 注意：这里使用 update 语句的返回值判断是否扣减成功（防止并发超卖）
        if(teamMapper.decreaseJobNumber(teamJobId) <= 0){
            throw new MyException("职位名额已满，录取失败");
        }

        if (remainingNumber != null && remainingNumber <= 1) {

            log.info("触发自动下架职位");

            // 获取当前登录的团队ID，用于权限校验（虽然这里是管理员或团队操作，但updateTeamJobStatus需要teamId）
            Long teamId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));

            //获取职位名称
            TeamJob currentJob = teamMapper.queryTeamJobById(teamJobId);
            // 更新状态为 5 (已下架/满员)
            teamMapper.updateTeamJobStatus(teamId, 5L, teamJobId);
            if (currentJob == null) {
                return Result.error("招聘职位不存在");
            }
            String jobName = currentJob.getName();

            log.info("开始发送下架通知");
            emailService.sendJobOfflineNotification(teamId, jobName);
            log.info("成功发送下架通知");
        }
        return Result.success("保存录取成功");
    }

    @Override
    public Result<String> updateInterviewStatus(HttpServletRequest httpServletRequest, Map<String, Object> data) {
        Long userId = Long.valueOf(String.valueOf(data.get("userId")));
        Long interviewId = Long.valueOf(String.valueOf(data.get("interviewId")));
        if(interViewMapper.updateInterviewStatusById(userId, interviewId, 2) <= 0){
            throw new MyException("更新投递面试状态出错");
        }
        return Result.success("更新面试状态成功");
    }

    @Override
    public Result<Map<String, Object>> searchInterview(HttpServletRequest httpServletRequest, String content) {
        Long teamId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        Map<String, Object> res = new HashMap<>();
        res.put("data", teamMapper.searchInterviewById(teamId, content));
        res.put("totalNum", teamMapper.searchInterviewTotalById(teamId, content));
        return Result.success(res);
    }

    @Override
    public Result<Map<String, Object>> getAdmissionInfo(HttpServletRequest httpServletRequest, int page, int pageSize) {
        Long teamId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        Map<String, Object> res = new HashMap<>();
        res.put("data", teamMapper.queryAdmissionById(teamId, (page - 1) * pageSize, pageSize));
        res.put("totalNum", teamMapper.queryAdmissionTotalById(teamId));
        return Result.success(res);
    }

    @Override
    public Result<Map<String, Object>> getAllAdmissionInfo(int page, int pageSize) {
        Map<String, Object> res = new HashMap<>();
        res.put("data", teamMapper.queryAllAdmission((page - 1) * pageSize, pageSize));
        res.put("totalNum", teamMapper.queryAllAdmissionTotal());
        return Result.success(res);
    }



    @Override
    public Result<Map<String, Object>> searchAdmission(HttpServletRequest httpServletRequest, String content) {
        Long teamId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        Map<String, Object> res = new HashMap<>();
        res.put("data", teamMapper.searchAdmissionById(teamId, content));
        res.put("totalNum", teamMapper.searchAdmissionTotalById(teamId, content));
        return Result.success(res);
    }

    @Override
    public Result<List<TeamJob>> recommendTeamJob() {
        return Result.success(teamMapper.queryRecommendTeamJob());
    }

    @Override
    public Result<List<TeamJob>> searchIndexTeamJob(String content) {
        return Result.success(teamMapper.searchTeamJobListByContent(content));
    }

    @Override
    public Result<Map<String, Object>> getTeamAll(HttpServletRequest httpServletRequest, int page, int pageSize) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        Map<String, Object> res = new HashMap<>();
        res.put("data", teamMapper.queryTeamAll((page - 1) * pageSize, pageSize));
        res.put("totalNum", teamMapper.queryTeamTotal());
        return Result.success(res);
    }

    @Override
    public Result<Map<String, Object>> getTeamJobExamine(HttpServletRequest httpServletRequest, int page, int pageSize) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        Map<String, Object> res = new HashMap<>();
        res.put("data", examineMapper.queryExamineAll((page - 1) * pageSize, pageSize));
        res.put("totalNum", examineMapper.queryExamineTotal());
        return Result.success(res);
    }

    @Override
    public Result<String> updateAgainChangeTeamJobStatus(HttpServletRequest httpServletRequest, Long teamJobId, Long status) {
        Long teamId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        if(teamMapper.updateTeamJobStatus(teamId, status, teamJobId) <= 0){
            throw new MyException("重新提交审核出错");
        }
        // 修改审核表的结果等待管理员审核
        if(examineMapper.updateResultByTeamJobId(teamJobId, status - 1) <= 0){
            return Result.error("修改审核状态出错");
        }
        return Result.success("重新提交审核成功");
    }

    @Override
    public Result<Map<String, Object>> adminSearchTeam(HttpServletRequest httpServletRequest, String content) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        Map<String, Object> res = new HashMap<>();
        List<Team> get = teamMapper.queryTeamByUsername(content);
        res.put("data", get);
        res.put("totalNum", get.size());

        return Result.success(res);
    }

    @Transactional
    @Override
    public Result<String> adminRemoveTeam(HttpServletRequest httpServletRequest, List<Long> data) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        // 删除
        for(Long id: data){
            if(teamMapper.updateTeamStatusById(id, 0) <= 0){
                throw new MyException("删除出错");
            }
        }
        return Result.success("删除成功");
    }

    @Override
    public Result<String> adminReturnTeam(HttpServletRequest httpServletRequest, List<Long> data) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        // 删除
        for(Long id: data){
            if(teamMapper.updateTeamStatusById(id, 1) <= 0){
                throw new MyException("激活出错");
            }
        }
        return Result.success("激活成功");
    }


    @Override
    public Result<String> adminResetTeam(HttpServletRequest httpServletRequest, List<Long> data) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        // 重置
        for(Long id: data){
            if(teamMapper.updateTeamPasswordById(id, DigestUtil.md5Hex(DigestUtil.md5Hex("12345678"))) <= 0){
                throw new MyException("重置密码出错");
            }
        }
        return Result.success("重置密码成功");
    }

    @Override
    public Result<Map<String, Object>> adminTeamJobExamineAll(HttpServletRequest httpServletRequest, int page, int pageSize) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        Map<String, Object> res = new HashMap<>();
        res.put("data", examineMapper.queryExamineAllByAdmin((page - 1) * pageSize, pageSize));
        res.put("totalNum", examineMapper.queryExamineTotal());
        return Result.success(res);
    }

    @Transactional
    @Override
    public Result<String> adminChangeExamineStatus(HttpServletRequest httpServletRequest, long id, int status, Long teamId, Long teamJobId) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        // 修改审核表
        if(examineMapper.updateResultStatusById(id, status, adminId) <= 0){
            throw new MyException("修改审核出错");
        }
        // 修改团队招聘信息
        if(adminMapper.updateTeamJobStatus(teamId, status + 1L, teamJobId) <= 0){
            throw new MyException("修改团队招聘信息状态出错");
        }
        return Result.success("审核成功");
    }

    @Override
    public Result<Map<String, Object>> adminSearchExamine(HttpServletRequest httpServletRequest, String content) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        Map<String, Object> res = new HashMap<>();
        List<TeamJob> get = examineMapper.searchAdminExamineListByContent(content);
        res.put("data", get);
        res.put("totalNum", get.size());
        return Result.success(res);
    }
}

