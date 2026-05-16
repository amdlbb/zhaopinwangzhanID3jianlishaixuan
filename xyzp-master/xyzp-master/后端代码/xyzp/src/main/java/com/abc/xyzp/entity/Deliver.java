package com.abc.xyzp.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Deliver {

    private Long id;

    private Long teamJobId;

    private Long userId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private int status;
}
