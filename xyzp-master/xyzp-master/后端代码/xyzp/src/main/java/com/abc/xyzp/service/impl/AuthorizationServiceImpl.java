package com.abc.xyzp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.abc.xyzp.VO.LoginSuccessVO;
import com.abc.xyzp.common.MyConstants;
import com.abc.xyzp.common.SnowFlake;
import com.abc.xyzp.common.exceptor.MyException;
import com.abc.xyzp.config.JwtConfig;
import com.abc.xyzp.dto.AdminDto;
import com.abc.xyzp.dto.UserDto;
import com.abc.xyzp.entity.*;
import com.abc.xyzp.mapper.AdminMapper;
import com.abc.xyzp.mapper.TeamMapper;
import com.abc.xyzp.mapper.UserMapper;
import com.abc.xyzp.mapper.UserResumeMapper;
import com.abc.xyzp.service.AuthorizationService;
import com.abc.xyzp.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private UserMapper userMapper;

    @Resource
    private TeamMapper teamMapper;

    @Resource
    private UserResumeMapper userResumeMapper;

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private EmailService emailService;

    @Override
    public Result<LoginSuccessVO>
    login(User user) {

        String token;

        // 如果 myChoice 未传递（为 null），则尝试在用户和团队中查找
        if (user.getMyChoice() == null) {

            log.info("用户名：{} 没有携带myChoice字段", user.getUsername());
            User trueUser = userMapper.queryUserByUsername(user.getUsername());
            Team trueTeam = teamMapper.queryUserByUsername(user.getUsername());

            // 优先判断用户
            if (trueUser != null) {
                user.setMyChoice(0);
                // 检查密码是否正确
                if (!trueUser.getPassword().equals(DigestUtil.md5Hex(user.getPassword()))) {
                    throw new MyException("账号或者密码错误");
                }

                // 去掉敏感信息
                UserDto userDto = BeanUtil.copyProperties(trueUser, UserDto.class);
                // 在redis中保存用户信息
                redisTemplate.opsForValue().set(MyConstants.USERINFO + trueUser.getId(), JSONUtil.toJsonStr(userDto), 1, TimeUnit.HOURS);
            }
            // 其次判断团队
            else if (trueTeam != null) {
                user.setMyChoice(1);
                // 检查密码是否正确
                if (!trueTeam.getPassword().equals(DigestUtil.md5Hex(user.getPassword()))) {
                    throw new MyException("账号或者密码错误");
                }

                // 去掉敏感信息
                UserDto userDto = BeanUtil.copyProperties(trueTeam, UserDto.class);
                // 在redis中保存用户信息
                redisTemplate.opsForValue().set(MyConstants.USERINFO + userDto.getId(), JSONUtil.toJsonStr(userDto), 1, TimeUnit.HOURS);
            }
            // 都没找到
            else {
                throw new MyException("账号或者密码错误");
            }
        }

        // 判断是团队还是用户还是管理员
        if(user.getMyChoice() == 0){
            User trueUser = userMapper.queryUserByUsername(user.getUsername());
            if(trueUser == null){
                throw new MyException("账号不存在");
            }
            // 检查是否登录正确
            else if(user.getMyChoice() != Integer.valueOf(String.valueOf(redisTemplate.opsForValue().get(MyConstants.MYCHOICE + trueUser.getId())))){
                throw new MyException("非法登录");
            }
            else if(!trueUser.getPassword().equals(DigestUtil.md5Hex(user.getPassword()))){
                throw new MyException("账号或者密码错误");
            }
            token = jwtConfig.createToken(String.valueOf(trueUser.getId()));

            // 去掉敏感信息
            UserDto userDto = BeanUtil.copyProperties(trueUser, UserDto.class);
            // 在redis中保存用户信息
            redisTemplate.opsForValue().set(MyConstants.USERINFO + trueUser.getId(), JSONUtil.toJsonStr(userDto), 1, TimeUnit.HOURS);
        }
        else if(user.getMyChoice() == 1){
            Team trueTeam = teamMapper.queryUserByUsername(user.getUsername());
            if(trueTeam == null){
                throw new MyException("账号不存在");
            }
            // 检查是否登录正确
            else if(user.getMyChoice() != Long.parseLong(String.valueOf(redisTemplate.opsForValue().get(MyConstants.MYCHOICE + trueTeam.getId())))){
                throw new MyException("非法登录");
            }
            else if(!trueTeam.getPassword().equals(DigestUtil.md5Hex(user.getPassword()))){
                throw new MyException("账号或者密码错误");
            }
            token = jwtConfig.createToken(String.valueOf(trueTeam.getId()));
            // 去掉敏感信息
            UserDto userDto = BeanUtil.copyProperties(trueTeam, UserDto.class);
            // 在redis中保存用户信息
            redisTemplate.opsForValue().set(MyConstants.USERINFO + userDto.getId(), JSONUtil.toJsonStr(userDto), 1, TimeUnit.HOURS);
        }
        else{
            Admin admin = adminMapper.queryAdminByUsername(user.getUsername());
            if(admin == null){
                throw new MyException("账号不存在");
            }
            else if(!admin.getPassword().equals(DigestUtil.md5Hex(user.getPassword()))){
                throw new MyException("账号或者密码错误");
            }
            token = jwtConfig.createToken(String.valueOf(admin.getId()));

            // 去掉敏感信息
            AdminDto adminDto = BeanUtil.copyProperties(admin, AdminDto.class);
            // 在redis中保存用户信息
            redisTemplate.opsForValue().set(MyConstants.USERINFO + adminDto.getId(), JSONUtil.toJsonStr(adminDto), 1, TimeUnit.HOURS);
        }
        return Result.success(new LoginSuccessVO(token, user.getMyChoice()));
    }

    @Override
    @Transactional
    public Result<String> register(RegisterForm registerForm) {
        // 检验验证码是否过期
        String uuid = registerForm.getUuid();
        String object = (String) redisTemplate.opsForValue().get(MyConstants.REGISTER_CODE + uuid);
        if(object == null){
            throw new MyException("验证码已过期，请重试");
        }
        else if(Integer.parseInt(object) != registerForm.getCode()){
            throw new MyException("验证码不一致");
        }

        User user = registerForm.getUser();

        Long id = SnowFlake.nextId();
        user.setId(id);
        user.setAvatar("http://localhost:8081/api/image/download/5db37bc4-f04c-4858-b624-307433b49e7a.png");
        user.setPassword(DigestUtil.md5Hex(user.getPassword()));

        // 判断是部门还是用户注册
        if(registerForm.getMyChoice() == 0){
            // 创建用户简历
            Long userResumeId = SnowFlake.nextId();
            UserResume userResume = new UserResume();
            userResume.setId(userResumeId);
            userResume.setStatus(1);
            // 保存简历
            if(userResumeMapper.save(userResume) <= 0){
                throw new MyException("创建用户简历出错");
            }

            user.setUserResumeId(userResumeId);
            // 检验账号是否存在
            if(userMapper.queryUserByUsername(user.getUsername()) != null){
                throw new MyException("账号已经存在");
            }
            user.setNickname("用户HelloWorld");
            if(userMapper.save(user) <= 0){
                throw new MyException("保存用户出错");
            }
            // 在redis中保存是部门还是用户
            redisTemplate.opsForValue().set(MyConstants.MYCHOICE + id, 0);


        }
        else{
            // 检验账号是否存在
            if(teamMapper.queryUserByUsername(user.getUsername()) != null){
                throw new MyException("账号已经存在");
            }
            user.setNickname("团队HelloWorld");
            if(teamMapper.save(user) <= 0){
                throw new MyException("保存出错");
            }

            // 在redis中保存是团队还是用户
            redisTemplate.opsForValue().set(MyConstants.MYCHOICE + id, 1);
        }
        return Result.success("注册成功");
    }

    @Override
    public Result<String> logout(HttpServletRequest httpServletRequest) {
        String userId = String.valueOf(httpServletRequest.getAttribute("userId"));
        // 删除redis中的数据
        redisTemplate.delete(MyConstants.USERINFO + userId);

        return Result.success("退出成功");
    }
}
