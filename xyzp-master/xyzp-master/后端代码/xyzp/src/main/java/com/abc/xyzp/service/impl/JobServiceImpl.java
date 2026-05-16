package com.abc.xyzp.service.impl;

import cn.hutool.json.JSONUtil;
import com.abc.xyzp.common.MyConstants;
import com.abc.xyzp.common.SnowFlake;
import com.abc.xyzp.common.exceptor.MyException;
import com.abc.xyzp.entity.*;
import com.abc.xyzp.mapper.AdminMapper;
import com.abc.xyzp.mapper.JobMapper;
import com.abc.xyzp.service.JobService;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class JobServiceImpl implements JobService {

    @Resource
    private JobMapper jobMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AdminMapper adminMapper;

    @Override
    public Result<List<JobData>> getAllJob() {
        // 先查找redis中是否存在缓存
        String jobStr = (String) redisTemplate.opsForValue().get(MyConstants.ALLJOB);
        if(jobStr != null){
            return Result.success(JSONUtil.toList(jobStr, JobData.class));
        }

        List<JobData> res = new ArrayList<>();

        List<Job> jobs = jobMapper.getAll();



        for(Job job: jobs){
            // 找第一类职位
            if(job.getPId() != null && job.getPId() == 0){
                JobData jobData1 = new JobData();
                jobData1.setName(job.getName());
                jobData1.setId(job.getId());
                jobData1.setList(new ArrayList<>());
                for(Job job2: jobs){
                    // 找第二类职位
                    if(job2.getPId() != null && job2.getPId().equals(job.getId())){
                        JobData jobData2 = new JobData();
                        jobData2.setList(new ArrayList<>());
                        jobData2.setId(job2.getId());
                        jobData2.setName(job2.getName());
                        for(Job job3: jobs){
                            // 找第三类职位
                            if(job3.getPId() != null && job3.getPId().equals(job2.getId())){
                                JobData jobData3 = new JobData();
                                jobData3.setId(job3.getId());
                                jobData3.setName(job3.getName());
                                jobData2.getList().add(jobData3);
                            }
                        }
                        jobData1.getList().add(jobData2);
                    }
                }
                res.add(jobData1);
            }
        }

        // 在redis中保存job数据
        redisTemplate.opsForValue().set(MyConstants.ALLJOB, JSONUtil.toJsonStr(res), 1, TimeUnit.DAYS);

        return Result.success(res);
    }


    @Override
    public Result<Map<String, Object>> adminTeamJobAll(HttpServletRequest httpServletRequest, int page, int pageSize) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        Map<String, Object> res = new HashMap<>();
        List<Job> get = jobMapper.queryJobTotalByAdmin((page - 1) * pageSize, pageSize);
        res.put("data", get);
        res.put("totalNum", jobMapper.queryJobTotalNumByAdmin());

        return Result.success(res);

    }

    @Override
    public Result<List<List<Job>>> getJobCategory(HttpServletRequest httpServletRequest) {
        List<List<Job>> res = new ArrayList<>();
        List<Job> li1 = jobMapper.queryJobCategory1();
        List<Job> li2 = jobMapper.queryJobCategory2();
        List<Job> li3 = jobMapper.queryJobCategory3();
        res.add(li1);
        res.add(li2);
        res.add(li3);
        return Result.success(res);
    }


    @Override
    public Result<String> updateJob(HttpServletRequest httpServletRequest, Job job) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        if(jobMapper.updateJobById(job) <= 0){
            return Result.error("保存职位出错");
        }
        // 删除redis中的数据，以便于更新
        redisTemplate.delete(MyConstants.ALLJOB);
        return Result.success("修改成功");
    }


    @Override
    public Result<Map<String, Object>> searchJob(HttpServletRequest httpServletRequest, String content) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        Map<String, Object> res = new HashMap<>();
        List<Job> get = jobMapper.queryNameByContent(content);
        res.put("data", get);
        res.put("totalNum", get.size());
        return Result.success(res);
    }

    @Transactional
    @Override
    public Result<String> removeJob(HttpServletRequest httpServletRequest, List<Job> data) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        for(Job job: data){
            if(jobMapper.updateJobStatusById(job.getId(), 0) <= 0){
                throw new MyException("修改职位状态出错");
            }
        }
        // 删除reids中的数据
        redisTemplate.delete(MyConstants.ALLJOB);
        return Result.success("删除成功");
    }


    @Override
    public Result<String> activeJob(HttpServletRequest httpServletRequest, Long id) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        if(jobMapper.updateJobStatusById(id, 1) <= 0){
            throw new MyException("修改职位状态出错");
        }
        // 删除reids中的数据
        redisTemplate.delete(MyConstants.ALLJOB);

        return Result.success("激活成功");
    }


    @Override
    public Result<String> save(HttpServletRequest httpServletRequest, Map<String, String> job) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        // 为空则为第一级别，否则第二第三
        if(job.get("value").equals("1")){
            if(jobMapper.save(job.get("name"), 0L, SnowFlake.nextId()) <= 0){
                throw new MyException("保存出错");
            }
        }
        else{
            if(jobMapper.save(job.get("name"), Long.parseLong(job.get("pId")), SnowFlake.nextId()) <= 0){
                throw new MyException("保存出错");
            }
        }
        // 删除redis数据
        redisTemplate.delete(MyConstants.ALLJOB);

        return Result.success("保存成功");
    }

    @Override
    public Result<Integer> getJobAllTest(HttpServletRequest httpServletRequest) {
        return Result.success(jobMapper.getTotalNumTest());
    }
}
