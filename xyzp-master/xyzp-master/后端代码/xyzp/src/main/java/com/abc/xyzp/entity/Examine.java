package com.abc.xyzp.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Examine {

    private Long id;

    private Long teamJobId;

    private Long adminId;

    private int result;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private int status;

}
