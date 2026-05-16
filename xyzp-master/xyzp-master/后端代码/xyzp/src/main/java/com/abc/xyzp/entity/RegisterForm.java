package com.abc.xyzp.entity;

import lombok.Data;

/**
 * 注册表单
 */
@Data
public class RegisterForm {

    private User user;

    private String uuid;

    private int code;

    // 0为用户，1为团队
    private int myChoice;
}
