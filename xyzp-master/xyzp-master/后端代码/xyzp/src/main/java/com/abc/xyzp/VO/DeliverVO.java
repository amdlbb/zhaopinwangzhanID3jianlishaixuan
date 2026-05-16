package com.abc.xyzp.VO;

import com.abc.xyzp.entity.Resume;
import com.abc.xyzp.entity.UserResume;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeliverVO {
    private Long id;

    private Long teamJobId;

    private Long userId;

    private Long teamId;

    private String username;

    private String nickname;

    private String jobname;

    private String teamname;

    private UserResume userResume;

    private Resume resume;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean predictResult;

}
