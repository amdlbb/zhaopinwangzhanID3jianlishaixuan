package com.abc.xyzp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {

    private Long id;

    private String username;

    private String nickname;

    private String introduce;

    private String email;

    private String avatar;

    private int myChoice;

    private Long userResumeId;

}
