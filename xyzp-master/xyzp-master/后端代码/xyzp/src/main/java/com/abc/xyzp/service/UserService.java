package com.abc.xyzp.service;

import com.abc.xyzp.dto.AdmissionDto;
import com.abc.xyzp.dto.UserDto;
import com.abc.xyzp.entity.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface UserService {

    Result<UserDto> getUserInfo(HttpServletRequest httpServletRequest);

    Result<UserDto> getUserInfo2(Long userId);

    Result<UserResume> getUserResume(HttpServletRequest httpServletRequest);

    Result<String> updateUserInfo(User user, HttpServletRequest httpServletRequest);

    Result<String> updateUserResume(UserResume userResume, HttpServletRequest httpServletRequest);

    Result<String> updateUserPassword(PasswordForm passwordForm, HttpServletRequest httpServletRequest);

    Result<Map<String, Object>> getUserDeliver(HttpServletRequest httpServletRequest, int page, int pageSize);

    Result<String> cancelUserDeliver(HttpServletRequest httpServletRequest, Long deliverId);

    Result<List<UserDeliver>> searchUserDeliver(HttpServletRequest httpServletRequest, String content);

    Result<Map<String, Object>> getUserALl(HttpServletRequest httpServletRequest, int page, int pageSize);

    Result<Map<String, Object>> adminSearchUser(HttpServletRequest httpServletRequest, String content);

    Result<String> adminRemoveUser(HttpServletRequest httpServletRequest, List<Long> data);

    Result<String> adminReturnUser(HttpServletRequest httpServletRequest, List<Long> data);

    Result<String> adminResetUser(HttpServletRequest httpServletRequest, List<Long> data);

    Result<String> saveUserDeliver(HttpServletRequest httpServletRequest, Long data);

    Result<Map<String, Object>> getAdmission(HttpServletRequest httpServletRequest, int page, int pageSize);

    Result<Map<String, Object>> searchUserAdmisson(HttpServletRequest httpServletRequest, String content);
}
