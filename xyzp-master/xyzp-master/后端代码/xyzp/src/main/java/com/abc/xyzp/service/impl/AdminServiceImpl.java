package com.abc.xyzp.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONString;
import cn.hutool.json.JSONUtil;
import com.abc.xyzp.common.MyConstants;
import com.abc.xyzp.common.SnowFlake;
import com.abc.xyzp.common.exceptor.MyException;
import com.abc.xyzp.dto.AdminDto;
import com.abc.xyzp.entity.*;
import com.abc.xyzp.mapper.*;
import com.abc.xyzp.service.AdminService;
import netscape.javascript.JSObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private TeamMapper teamMapper;

    @Resource
    private ExamineMapper examineMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private JobMapper jobMapper;

    @Override
    public Result<AdminDto> getAdminInfo(HttpServletRequest httpServletRequest) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        AdminDto adminDto = JSONUtil.toBean((String) redisTemplate.opsForValue().get(MyConstants.USERINFO + adminId), AdminDto.class);
        return Result.success(adminDto);
    }

    @Override
    public Result<String> updateExamineResult(HttpServletRequest httpServletRequest, Long examineId, int result) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        // 修改审核表
        if(examineMapper.updateResultById(examineId, result) <= 0){
            return Result.error("修改审核结果出错");
        }
        // 修改团队招聘状态
        Examine examine = examineMapper.queryExamineById(examineId);
        TeamJob teamJob = adminMapper.queryTeamJobById(examine.getTeamJobId());
        Long teamJobResult = result + 1L;
        if(adminMapper.updateTeamJobStatus(teamJob.getTeamId(), teamJobResult, teamJob.getId()) <= 0){
            return Result.error("修改团队招聘状态出错");
        }
        return Result.success("修改成功");
    }

    @Override
    public Result<Map<String, Object>> reqIndexInfo(HttpServletRequest httpServletRequest) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        Map<String, Object> res = new HashMap<>();
        // 查询用户数
        res.put("userNum", userMapper.queryUserTotal());
        // 查询团队数
        res.put("teamNum", teamMapper.queryTeamTotal());
        // 查询职位数
        res.put("jobNum", jobMapper.queryJobTotal());
        // 查询审核数
        res.put("examineNum", examineMapper.queryExamineTotal());

        return Result.success(res);

    }

    @Override
    public Result<Map<String, Object>> getAdminAll(HttpServletRequest httpServletRequest, int page, int pageSize) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        Map<String, Object> res = new HashMap<>();
        res.put("data", adminMapper.queryAdminAll((page - 1) * pageSize, pageSize));
        res.put("totalNum", adminMapper.queryAdminTotal());
        return Result.success(res);
    }

    @Override
    public Result<String> saveAdmin(HttpServletRequest httpServletRequest, Admin admin) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin findAdmin = adminMapper.queryAdminById(adminId);
        if(findAdmin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        admin.setId(SnowFlake.nextId());
        admin.setPassword(DigestUtil.md5Hex(admin.getPassword()));
        if(adminMapper.save(admin) <= 0){
            throw new MyException("保存管理员出错");
        }

        return Result.success("保存成功");
    }

    @Override
    public Result<Map<String, Object>> searchAdmin(HttpServletRequest httpServletRequest, String content) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin findAdmin = adminMapper.queryAdminById(adminId);
        if(findAdmin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        Map<String, Object> res = new HashMap<>();
        List<Admin> get = adminMapper.queryAdminLikeByUsername(content);
        res.put("data", get);
        res.put("totalNum", get.size());

        return Result.success(res);
    }

    @Override
    public Result<String> resetAdmin(HttpServletRequest httpServletRequest, List<Long> data) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        // 重置
        for(Long id: data){
            if(adminMapper.updateAdminPasswordById(id, DigestUtil.md5Hex(DigestUtil.md5Hex("12345678"))) <= 0){
                throw new MyException("重置密码出错");
            }
        }
        return Result.success("重置密码成功");
    }

    @Override
    public Result<String> returnAdmin(HttpServletRequest httpServletRequest, List<Long> data) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        // 重置
        for(Long id: data){
            if(adminMapper.updateAdminStatusById(id, 1) <= 0){
                throw new MyException("激活出错");
            }
        }
        return Result.success("激活成功");
    }

    @Override
    public Result<String> removeAdmin(HttpServletRequest httpServletRequest, List<Long> data) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        // 重置
        for(Long id: data){
            if(adminMapper.updateAdminStatusById(id, 0) <= 0){
                throw new MyException("删除出错");
            }
        }
        return Result.success("删除成功");
    }


    @Override
    public Result<String> updateAdminInfo(HttpServletRequest httpServletRequest, Admin admin) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin getAdmin = adminMapper.queryAdminById(adminId);
        if(getAdmin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        admin.setPassword(DigestUtil.md5Hex(admin.getPassword()));
        if(adminMapper.updateAdmin(admin) <= 0){
            throw new MyException("更新出错");
        }
        System.out.println(admin);
        return Result.success("更新成功");
    }
}
