package com.abc.xyzp.service;

import com.abc.xyzp.dto.TeamDto;
import com.abc.xyzp.entity.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface TeamService {

    Result<TeamDto> getTeamInfo(HttpServletRequest httpServletRequest);

    Result<TeamDto> getTeamInfo2(HttpServletRequest httpServletRequest, Long id);

    Result<String> updateTeamInfo(Team team, HttpServletRequest httpServletRequest);

    Result<String> updateTeamPassword(PasswordForm passwordForm, HttpServletRequest httpServletRequest);

    Result<String> saveTeamJob(HttpServletRequest httpServletRequest, TeamJob teamJob);

    Result<Map<String, Object>> getTeamJob(HttpServletRequest httpServletRequest, int page, int pageSize);

    Result<Map<String, Object>> getNewTeamJob(int page, int pageSize);

    Result<TeamJob> getTeamJobInfo(Long id);

    Result<String> updateTeamJob(HttpServletRequest httpServletRequest, TeamJob teamJob);

    Result<Map<String, Object>> getUserResume(HttpServletRequest httpServletRequest, int page, int pageSize);

    Result<String> updateTeamJobStatus(HttpServletRequest httpServletRequest, Long id, Long status);

    Result<String> saveInterView(HttpServletRequest httpServletRequest, Map<String, Object> data);

    Result<String> updateDeliverStatus(HttpServletRequest httpServletRequest, Map<String, Object> data);

    Result<Map<String, Object>> searchTeamJob(HttpServletRequest httpServletRequest, String content);

    Result<Map<String, Object>> searchUserResume(HttpServletRequest httpServletRequest, String content);

    Result<Map<String, Object>> getInterviewInfo(HttpServletRequest httpServletRequest, int page, int pageSize);

    Result<String> admissionUser(HttpServletRequest httpServletRequest, Map<String, Object> data);

    Result<String> updateInterviewStatus(HttpServletRequest httpServletRequest, Map<String, Object> data);

    Result<Map<String, Object>> searchInterview(HttpServletRequest httpServletRequest, String content);

    Result<Map<String, Object>> getAdmissionInfo(HttpServletRequest httpServletRequest, int page, int pageSize);

    Result<Map<String, Object>> getAllAdmissionInfo(int page, int pageSize);

    Result<Map<String, Object>> searchAdmission(HttpServletRequest httpServletRequest, String content);

    Result<List<TeamJob>> recommendTeamJob();

    Result<List<TeamJob>> searchIndexTeamJob(String content);

    Result<Map<String, Object>> getTeamAll(HttpServletRequest httpServletRequest, int page, int pageSize);

    Result<Map<String, Object>> getTeamJobExamine(HttpServletRequest httpServletRequest, int page, int pageSize);

    Result<String> updateAgainChangeTeamJobStatus(HttpServletRequest httpServletRequest, Long teamJobId, Long status);

    Result<Map<String, Object>> adminSearchTeam(HttpServletRequest httpServletRequest, String content);

    Result<String> adminRemoveTeam(HttpServletRequest httpServletRequest, List<Long> data);

    Result<String> adminReturnTeam(HttpServletRequest httpServletRequest, List<Long> data);

    Result<String> adminResetTeam(HttpServletRequest httpServletRequest, List<Long> data);

    Result<Map<String, Object>> adminTeamJobExamineAll(HttpServletRequest httpServletRequest, int page, int pageSize);

    Result<String> adminChangeExamineStatus(HttpServletRequest httpServletRequest, long id, int status, Long teamId, Long teamJobId);

    Result<Map<String, Object>> adminSearchExamine(HttpServletRequest httpServletRequest, String content);
}
