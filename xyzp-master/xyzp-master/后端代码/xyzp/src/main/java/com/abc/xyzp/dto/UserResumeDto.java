package com.abc.xyzp.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResumeDto {

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

    private String jobName;

    private Long teamJobId;

    private Long userId;

    private Long deliverId;
}
