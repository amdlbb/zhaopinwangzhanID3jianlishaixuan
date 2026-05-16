package com.abc.xyzp.service;

import com.abc.xyzp.entity.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface InterviewService {

    Result<Map<String, Object>> getInterviewAll(HttpServletRequest httpServletRequest, int page, int pageSize);
}
