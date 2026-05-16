package com.abc.xyzp.mapper;

import com.abc.xyzp.entity.UserResume;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface UserResumeMapper {

    @Insert("insert into tb_user_resume (id, status) values (#{id}, #{status})")
    int save(UserResume userResume);



}
