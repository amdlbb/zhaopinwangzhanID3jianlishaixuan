package com.abc.xyzp.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeamJob {

    private Long id;

    private Long teamId;

    private Long jobId;

    private String salary;

    private int number;

    private String address;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private int status;


    // 其他属性
    private String nickname;

    private String content;

    private String education;

    private String name;

    private String avatar;

    private String introduce;

}
