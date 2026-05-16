package com.abc.xyzp.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Interview {

    private Long id;

    private Long teamJobId;

    private Long userId;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private String address;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private int status;
}
