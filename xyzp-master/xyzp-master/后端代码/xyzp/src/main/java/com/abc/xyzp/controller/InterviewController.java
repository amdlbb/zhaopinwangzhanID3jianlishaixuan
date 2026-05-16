package com.abc.xyzp.controller;


import com.abc.xyzp.entity.Result;
import com.abc.xyzp.service.InterviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/interview")
public class InterviewController {

    @Resource
    private InterviewService interviewService;

    @GetMapping("/getInterviewAll")
    public Result<Map<String, Object>> getInterviewAll(HttpServletRequest httpServletRequest,
                                                       @RequestParam("page") int page,
                                                       @RequestParam("pageSize") int pageSize){
        return interviewService.getInterviewAll(httpServletRequest, page, pageSize);
    }

}
