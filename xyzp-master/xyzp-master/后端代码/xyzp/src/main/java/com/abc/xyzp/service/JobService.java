package com.abc.xyzp.service;

import com.abc.xyzp.entity.Job;
import com.abc.xyzp.entity.JobData;
import com.abc.xyzp.entity.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface JobService {

    Result<List<JobData>> getAllJob();

    Result<Map<String, Object>> adminTeamJobAll(HttpServletRequest httpServletRequest, int page, int pageSize);

    Result<List<List<Job>>> getJobCategory(HttpServletRequest httpServletRequest);

    Result<String> updateJob(HttpServletRequest httpServletRequest, Job job);

    Result<Map<String, Object>> searchJob(HttpServletRequest httpServletRequest, String content);

    Result<String> removeJob(HttpServletRequest httpServletRequest, List<Job> data);

    Result<String> activeJob(HttpServletRequest httpServletRequest, Long id);

    Result<String> save(HttpServletRequest httpServletRequest, Map<String, String> job);

    Result<Integer> getJobAllTest(HttpServletRequest httpServletRequest);
}
