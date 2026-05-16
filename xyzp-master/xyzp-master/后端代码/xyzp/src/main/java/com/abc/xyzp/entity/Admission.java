package com.abc.xyzp.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Admission {

    private Long id;

    private Long teamJobId;

    private Long userId;

    private LocalDateTime workTime;

    private String workAddress;

    private int status;
}
