package com.abc.xyzp.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Job {

    private Long id;

    private String name;

    private Long pId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private int status;

    private String pJobName;

}
