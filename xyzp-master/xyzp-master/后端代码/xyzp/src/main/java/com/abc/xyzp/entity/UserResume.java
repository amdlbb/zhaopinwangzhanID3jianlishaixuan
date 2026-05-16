package com.abc.xyzp.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResume {

    private Long id;

    private String name;

    private int age;

    private String phone;

    private String major;

    private String education;

    private String school;

    private String content;

    private String sex;

    private String exceptionSalary;

    private String exceptionJob;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private int status;

}
