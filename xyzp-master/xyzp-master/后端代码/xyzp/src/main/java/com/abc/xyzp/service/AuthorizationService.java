package com.abc.xyzp.service;

import com.abc.xyzp.VO.LoginSuccessVO;
import com.abc.xyzp.entity.RegisterForm;
import com.abc.xyzp.entity.Result;
import com.abc.xyzp.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface AuthorizationService {

    Result<LoginSuccessVO> login(User user);

    Result<String> register(RegisterForm registerForm);

    Result<String> logout(HttpServletRequest httpServletRequest);
}
