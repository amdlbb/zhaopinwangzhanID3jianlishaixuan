package com.abc.xyzp.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Team {

    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String introduce;

    private String email;

    private String avatar;

    private String address;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private int status;

}
