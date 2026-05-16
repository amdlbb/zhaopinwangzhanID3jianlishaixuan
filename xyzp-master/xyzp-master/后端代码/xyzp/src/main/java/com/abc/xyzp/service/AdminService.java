package com.abc.xyzp.service;

import com.abc.xyzp.dto.AdminDto;
import com.abc.xyzp.entity.Admin;
import com.abc.xyzp.entity.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface AdminService {
    Result<AdminDto> getAdminInfo(HttpServletRequest httpServletRequest);

    Result<String> updateExamineResult(HttpServletRequest httpServletRequest, Long examineId, int result);

    Result<Map<String, Object>> reqIndexInfo(HttpServletRequest httpServletRequest);

    Result<Map<String, Object>> getAdminAll(HttpServletRequest httpServletRequest, int page, int pageSize);

    Result<String> saveAdmin(HttpServletRequest httpServletRequest, Admin admin);

    Result<Map<String, Object>> searchAdmin(HttpServletRequest httpServletRequest, String content);

    Result<String> resetAdmin(HttpServletRequest httpServletRequest, List<Long> data);

    Result<String> returnAdmin(HttpServletRequest httpServletRequest, List<Long> data);

    Result<String> removeAdmin(HttpServletRequest httpServletRequest, List<Long> data);

    Result<String> updateAdminInfo(HttpServletRequest httpServletRequest, Admin admin);
}
