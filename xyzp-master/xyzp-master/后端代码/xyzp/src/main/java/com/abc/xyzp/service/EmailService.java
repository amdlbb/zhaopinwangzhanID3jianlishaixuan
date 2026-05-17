package com.abc.xyzp.service;

import com.abc.xyzp.entity.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface EmailService {
    Result<String> send(String uuid, String email);

    Result<String> sendInterview(HttpServletRequest httpServletRequest, Map<String, Object> data);

    Result<String> sendAdmission(HttpServletRequest httpServletRequest, Map<String, Object> data);

    Result<String> sendJobOfflineNotification(Long teamId, String jobName);

    Result<String> sendOnboardEmail(String title, String content, String toEmail);
}
