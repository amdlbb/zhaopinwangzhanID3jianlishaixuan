package com.abc.xyzp.controller;

import com.abc.xyzp.entity.Job;
import com.abc.xyzp.entity.JobData;
import com.abc.xyzp.entity.Result;
import com.abc.xyzp.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private JobService jobService;

    /**
     * 获取所有的职位
     * @return
     */
    @GetMapping
    public Result<List<JobData>> getAllJob(){
        return jobService.getAllJob();
    }


    /**
     * 管理员获取所有职位
     * @param httpServletRequest
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/adminJobAll")
    public Result<Map<String, Object>> adminTeamJobAll(HttpServletRequest httpServletRequest,
                                                       @RequestParam("page") int page,
                                                       @RequestParam("pageSize") int pageSize){
        return jobService.adminTeamJobAll(httpServletRequest, page, pageSize);
    }


    /**
     * 获取所有职位各个类别的列表
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/getJobCategory")
    public Result<List<List<Job>>> getJobCategory(HttpServletRequest httpServletRequest){
        return jobService.getJobCategory(httpServletRequest);
    }


    /**
     * 修改职位信息
     * @param httpServletRequest
     * @return
     */
    @PutMapping("/updateJob")
    public Result<String> updateJob(HttpServletRequest httpServletRequest,
                                    @RequestBody Job job){
        return jobService.updateJob(httpServletRequest, job);
    }


    /**
     * 管理员搜索职位
     * @param httpServletRequest
     * @param content
     * @return
     */
    @GetMapping("/searchJob")
    public Result<Map<String, Object>> searchJob(HttpServletRequest httpServletRequest,
                                    @RequestParam("content") String content){
        return jobService.searchJob(httpServletRequest, content);
    }


    /**
     * 删除职位
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PostMapping("/removeJob")
    public Result<String> removeJob(HttpServletRequest httpServletRequest,
                                    @RequestBody List<Job> data){
        return jobService.removeJob(httpServletRequest, data);
    }


    /**
     * 激活职位
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PostMapping("/activeJob")
    public Result<String> activeJob(HttpServletRequest httpServletRequest,
                                    @RequestBody Map<String, Long> data){
        return jobService.activeJob(httpServletRequest, data.get("id"));
    }


    /**
     * 保存职位信息
     * @param httpServletRequest
     * @param job
     * @return
     */
    @PostMapping("/save")
    public Result<String> save(HttpServletRequest httpServletRequest,
                                    @RequestBody Map<String, String> job){
        return jobService.save(httpServletRequest, job);
    }


    /**
     * 获取职位总数
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/getJobAllTest")
    public Result<Integer> getJobAllTest(HttpServletRequest httpServletRequest){
        System.out.println(jobService.getJobAllTest(httpServletRequest));
        return jobService.getJobAllTest(httpServletRequest);
    }




}
