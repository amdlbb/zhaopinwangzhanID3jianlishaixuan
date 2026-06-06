package com.abc.xyzp.controller;

import com.abc.xyzp.VO.LoginSuccessVO;
import com.abc.xyzp.entity.RegisterForm;
import com.abc.xyzp.entity.Result;
import com.abc.xyzp.entity.User;
import com.abc.xyzp.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/authorization")
public class AuthorizationController {

    @Autowired
    private AuthorizationService authorizationService;

    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result<LoginSuccessVO> login(@RequestBody User user){
        return authorizationService.login(user);
    }

    /**
     * 注册
     * @param registerForm
     * @return
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody RegisterForm registerForm){
        return authorizationService.register(registerForm);
    }

    /**
     * 退出
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest httpServletRequest){
        return authorizationService.logout(httpServletRequest);
    }

    /**
     * pyfegin检查登录状态
     * @param token
     * @return
     */
    @GetMapping("/check-login")
    public Result<Map<String, Object>> checkLogin(
            @RequestHeader(value = "token", required = false) String token) {

        Map<String, Object> loginStatus = authorizationService.checkLoginStatus(token);
        return Result.success(loginStatus);
    }


}
