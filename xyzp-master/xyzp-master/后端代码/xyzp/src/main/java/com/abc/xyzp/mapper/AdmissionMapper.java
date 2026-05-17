package com.abc.xyzp.mapper;

import com.abc.xyzp.entity.Admission;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionMapper {
    @Insert("insert into tb_admission (id, team_job_id, user_id, work_time, work_address) values " +
            "(#{id}, #{teamJobId}, #{userId}, #{workTime}, #{workAddress})")
    int save(Admission admission);

    @Update("update tb_admission set status = #{status} where id = #{id}")
    int updateStatus(Long id, int status);
}
