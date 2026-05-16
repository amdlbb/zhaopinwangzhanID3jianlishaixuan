package com.abc.xyzp.VO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class interviewVO {

    private Long id;

    private Long teamJobId;

    private Long userId;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private String address;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private int status;

    //用户账户
    private String username;

    private String teamname;

    private String jobname;
}
