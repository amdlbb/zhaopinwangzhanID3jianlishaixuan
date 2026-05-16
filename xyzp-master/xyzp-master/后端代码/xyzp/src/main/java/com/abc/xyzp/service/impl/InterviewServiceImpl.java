package com.abc.xyzp.service.impl;


import com.abc.xyzp.VO.interviewVO;
import com.abc.xyzp.entity.Admin;
import com.abc.xyzp.entity.Result;
import com.abc.xyzp.mapper.AdminMapper;
import com.abc.xyzp.mapper.InterViewMapper;
import com.abc.xyzp.service.InterviewService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InterviewServiceImpl implements InterviewService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private InterViewMapper interViewMapper;

    @Override
    public Result<Map<String, Object>> getInterviewAll(HttpServletRequest httpServletRequest, int page, int pageSize) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        Map<String, Object> res = new HashMap<>();
        List<interviewVO> interviewVOList = interViewMapper.queryInterviewVOAll((page - 1) * pageSize, pageSize);
        res.put("data", interviewVOList);
        res.put("totalNum", interViewMapper.queryInterviewVOTotal());
        return Result.success(res);
    }
}
