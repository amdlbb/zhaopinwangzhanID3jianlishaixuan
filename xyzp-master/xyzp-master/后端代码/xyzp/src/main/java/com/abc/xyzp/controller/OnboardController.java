package com.abc.xyzp.controller;

import com.abc.xyzp.entity.Result;
import com.abc.xyzp.entity.User;
import com.abc.xyzp.mapper.AdmissionMapper;
import com.abc.xyzp.mapper.UserMapper;
import com.abc.xyzp.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/onboard")
@Slf4j
public class OnboardController {

    @Resource
    private AdmissionMapper admissionMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private EmailService emailService;

    /**
     * 设为入职
     * 将 tb_admission 中 status=1（未入职）改为 8（已入职）
     * @param data 包含 admissionIds 列表
     * @return
     */
    @PutMapping("/setOnboard")
    public Result<String> setOnboard(@RequestBody Map<String, Object> data) {
        try {
            @SuppressWarnings("unchecked")
            List<?> rawIds = (List<?>) data.get("admissionIds");
            if (rawIds == null || rawIds.isEmpty()) {
                return Result.error("请选择需要设为入职的记录");
            }
            for (Object rawId : rawIds) {
                Long id = Long.valueOf(String.valueOf(rawId));
                if (admissionMapper.updateStatus(id, 8) <= 0) {
                    log.warn("更新入职状态失败: admissionId={}", id);
                }
            }
            return Result.success("设为入职成功");
        } catch (Exception e) {
            log.error("设为入职异常", e);
            return Result.error("设为入职失败: " + e.getMessage());
        }
    }

    /**
     * 发送入职沟通邮件
     * @param data 包含 title, content, userId
     * @return
     */
    @PostMapping("/sendEmail")
    public Result<String> sendEmail(@RequestBody Map<String, Object> data) {
        try {
            String title = String.valueOf(data.get("title"));
            String content = String.valueOf(data.get("content"));
            Long userId = Long.valueOf(String.valueOf(data.get("userId")));

            // 查询用户邮箱
            User user = userMapper.queryUserById(userId);
            if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
                return Result.error("未找到该用户的邮箱信息");
            }

            return emailService.sendOnboardEmail(title, content, user.getEmail());
        } catch (Exception e) {
            log.error("发送入职邮件异常", e);
            return Result.error("发送邮件失败: " + e.getMessage());
        }
    }
}
