package com.abc.xyzp.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {

    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String introduce;

    private String email;

    private String avatar;

    private Long userResumeId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private int status;

    /*登录类型
    *   普通用户 (myChoice == 0
    *   团队 (myChoice == 1)
    *   管理员 (else)*/
    private Integer myChoice;

    public User(Long userId, String url) {
        this.id = userId;
        this.avatar = url;
    }
}
