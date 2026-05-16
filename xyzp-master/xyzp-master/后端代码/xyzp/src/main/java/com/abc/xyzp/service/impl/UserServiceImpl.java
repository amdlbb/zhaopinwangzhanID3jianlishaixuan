package com.abc.xyzp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.abc.xyzp.common.MyConstants;
import com.abc.xyzp.common.SnowFlake;
import com.abc.xyzp.common.exceptor.MyException;
import com.abc.xyzp.dto.AdmissionDto;
import com.abc.xyzp.dto.UserDto;
import com.abc.xyzp.entity.*;
import com.abc.xyzp.mapper.AdminMapper;
import com.abc.xyzp.mapper.DeliverMapper;
import com.abc.xyzp.mapper.UserMapper;
import com.abc.xyzp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private UserMapper userMapper;

    @Resource
    private DeliverMapper deliverMapper;

    @Resource
    private AdminMapper adminMapper;


    @Override
    public Result<UserDto> getUserInfo(HttpServletRequest httpServletRequest) {
        UserDto user;
        String id = (String) httpServletRequest.getAttribute("userId");
        String userStr = (String) redisTemplate.opsForValue().get(MyConstants.USERINFO + id);

        // 先看redis中是否有数据
        if(userStr == null){
            user = BeanUtil.copyProperties(userMapper.queryUserById(Long.valueOf(id)), UserDto.class);
            redisTemplate.opsForValue().set(MyConstants.USERINFO + id, JSONUtil.toJsonStr(user), 1, TimeUnit.HOURS);
        }
        else{
            user = JSONUtil.toBean(userStr, UserDto.class);
        }
        return Result.success(user);
    }

    @Override
    public Result<UserDto> getUserInfo2(Long userId) {
        UserDto user;
        String userStr = (String) redisTemplate.opsForValue().get(MyConstants.USERINFO + userId);
        if(userStr == null){
            User user1 = userMapper.queryUserById(Long.valueOf(userId));
            // 检查用户是否存在
            if(user1 == null){
                throw new MyException("用户不存在");
            }
            user = BeanUtil.copyProperties(user1, UserDto.class);
            redisTemplate.opsForValue().set(MyConstants.USERINFO + userId, JSONUtil.toJsonStr(user), 1, TimeUnit.HOURS);
        }
        else{
            user = JSONUtil.toBean(userStr, UserDto.class);
        }

        return Result.success(user);
    }


    @Override
    public Result<UserResume> getUserResume(HttpServletRequest httpServletRequest) {

        String userId = String.valueOf(httpServletRequest.getAttribute("userId"));
        // 找到redis中的数据
        Long userResumeId = JSONUtil.toBean(String.valueOf(redisTemplate.opsForValue().get(MyConstants.USERINFO + userId)), User.class).getUserResumeId();

        return Result.success(userMapper.queryUserResumeById(userResumeId));
    }

    @Override
    @Transactional
    public Result<String> updateUserInfo(User user, HttpServletRequest httpServletRequest) {
        // 验证身份
        Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        if(!userId.equals(user.getId())){
            throw new MyException("身份验证失败");
        }
        // 保存修改后的结果
        if(userMapper.updateById(user) <= 0){
            throw new MyException("更新出错");
        }
        // 更新reids
        UserDto userDto = BeanUtil.copyProperties(user, UserDto.class);
        redisTemplate.opsForValue().set(MyConstants.USERINFO + user.getId(), JSONUtil.toJsonStr(userDto), 1, TimeUnit.HOURS);
        return Result.success("更新成功");
    }

    @Override
    public Result<String> updateUserResume(UserResume userResume, HttpServletRequest httpServletRequest) {
        Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 验证身份
        UserDto userInfo = JSONUtil.toBean(String.valueOf(redisTemplate.opsForValue().get(MyConstants.USERINFO + userId)), UserDto.class);
        if(!userId.equals(userInfo.getId())){
            throw new MyException("身份验证失败");
        }
        // 更新简历
        if(userMapper.updateUserResumeById(userResume) <= 0){
            throw new MyException("更新用户简历出错");
        }

        return Result.success("更新用户简历成功");
    }


    @Override
    public Result<String> updateUserPassword(PasswordForm passwordForm, HttpServletRequest httpServletRequest) {
        Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 检验密码是否正确
        if(!DigestUtil.md5Hex(passwordForm.getBeforePassword()).equals(userMapper.queryUserById(userId).getPassword())){
            throw new MyException("密码不正确");
        }
        // 更新密码
        if(userMapper.updatePasswordById(userId, DigestUtil.md5Hex(passwordForm.getNowPassword())) <= 0){
            throw new MyException("更新密码出错");
        }
        return Result.success("更新密码成功");
    }

    @Override
    public Result<Map<String, Object>> getUserDeliver(HttpServletRequest httpServletRequest, int page, int pageSize) {
        Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        Map<String, Object> res = new HashMap<>();
        res.put("data", userMapper.queryUserDeliverById(userId, (page - 1) * pageSize, pageSize));
        res.put("totalNum", userMapper.queryUserDeliverTotalById(userId));
        return Result.success(res);
    }


    @Override
    public Result<String> cancelUserDeliver(HttpServletRequest httpServletRequest, Long deliverId) {
        Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        if(deliverMapper.updateUserDeliverStatusById(userId, deliverId, 0) < 0){
            throw new MyException("取消投递出错");
        }
        return Result.success("取消成功");
    }


    @Override
    public Result<List<UserDeliver>> searchUserDeliver(HttpServletRequest httpServletRequest, String content) {
        Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        return Result.success(userMapper.queryUserDeliverByContent(content, userId));
    }

    @Override
    public Result<Map<String, Object>> getUserALl(HttpServletRequest httpServletRequest, int page, int pageSize) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        Map<String, Object> res = new HashMap<>();
        res.put("data", userMapper.queryUserAll((page - 1) * pageSize, pageSize));
        res.put("totalNum", userMapper.queryUserTotal());
        return Result.success(res);
    }

    @Override
    public Result<Map<String, Object>> adminSearchUser(HttpServletRequest httpServletRequest, String content) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        Map<String, Object> res = new HashMap<>();
        List<User> get = userMapper.queryUserLikeByUsername(content);
        res.put("data", get);
        res.put("totalNum", get.size());

        return Result.success(res);
    }

    @Override
    public Result<String> adminRemoveUser(HttpServletRequest httpServletRequest, List<Long> data) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        // 删除
        for(Long id: data){
            if(userMapper.updateUserStatusById(id, 0) <= 0){
                throw new MyException("删除出错");
            }
        }
        return Result.success("删除成功");
    }


    @Override
    public Result<String> adminReturnUser(HttpServletRequest httpServletRequest, List<Long> data) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        // 删除
        for(Long id: data){
            if(userMapper.updateUserStatusById(id, 1) <= 0){
                throw new MyException("激活出错");
            }
        }
        return Result.success("激活成功");
    }

    @Override
    public Result<String> adminResetUser(HttpServletRequest httpServletRequest, List<Long> data) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin admin = adminMapper.queryAdminById(adminId);
        if(admin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }
        // 重置
        for(Long id: data){
            if(userMapper.updatePasswordById(id, DigestUtil.md5Hex(DigestUtil.md5Hex("12345678"))) <= 0){
                throw new MyException("重置密码出错");
            }
        }
        return Result.success("重置密码成功");
    }

    @Override
    public Result<String> saveUserDeliver(HttpServletRequest httpServletRequest, Long data) {
        Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 验证身份
        UserDto userInfo = JSONUtil.toBean(String.valueOf(redisTemplate.opsForValue().get(MyConstants.USERINFO + userId)), UserDto.class);
        if(!userId.equals(userInfo.getId())){
            throw new MyException("身份验证失败");
        }
        // 检查用户是否已经投递
        Deliver myDeliver = deliverMapper.queryDeliverByUserIdAndTeamJobId(userId, data);
        if(myDeliver != null){
            return Result.error("用户已经投递了，无需重复投递");
        }
        Deliver deliver = new Deliver();
        deliver.setTeamJobId(data);
        deliver.setUserId(userId);
        deliver.setId(SnowFlake.nextId());
        deliver.setStatus(1);
        // 保存投递
        if(deliverMapper.save(deliver) <= 0){
            return Result.error("投递出错");
        }
        return Result.success("投递成功");
    }


    @Override
    public Result<Map<String, Object>> getAdmission(HttpServletRequest httpServletRequest, int page, int pageSize) {
        Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 验证身份
        UserDto userInfo = JSONUtil.toBean(String.valueOf(redisTemplate.opsForValue().get(MyConstants.USERINFO + userId)), UserDto.class);
        if(!userId.equals(userInfo.getId())){
            throw new MyException("身份验证失败");
        }
        Map<String, Object> res = new HashMap<>();
        res.put("data", userMapper.queryUserAdmissionByUserId(userId));
        res.put("totalNum", res.size());
        return Result.success(res);
    }


    @Override
    public Result<Map<String, Object>> searchUserAdmisson(HttpServletRequest httpServletRequest, String content) {
        Long userId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 验证身份
        UserDto userInfo = JSONUtil.toBean(String.valueOf(redisTemplate.opsForValue().get(MyConstants.USERINFO + userId)), UserDto.class);
        if(!userId.equals(userInfo.getId())){
            throw new MyException("身份验证失败");
        }
        Map<String, Object> res = new HashMap<>();
        List<Map<String, Object>> get = userMapper.queryUserAdmissionByContent(userId, content);
        res.put("data", get);
        res.put("totalNum", get.size());
        return Result.success(res);
    }
}

