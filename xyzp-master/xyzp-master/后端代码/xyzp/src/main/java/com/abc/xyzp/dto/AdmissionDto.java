package com.abc.xyzp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdmissionDto {

    private Long teamJobId;

    private Long userId;

    private LocalDateTime workTime;

    private String workAddress;

    private int status;

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

    private String jobName;

}
