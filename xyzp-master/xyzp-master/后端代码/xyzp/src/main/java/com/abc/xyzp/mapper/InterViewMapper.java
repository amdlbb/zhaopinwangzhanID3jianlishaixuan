package com.abc.xyzp.mapper;

import com.abc.xyzp.VO.interviewVO;
import com.abc.xyzp.entity.Interview;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterViewMapper {
    @Insert("insert into tb_interview (id, team_job_id, user_id, begin_time, end_time, address) values (#{id}, #{teamJobId}, #{userId}, #{beginTime}, #{endTime}, #{address}) ")
    int save(Interview interView);

    @Update("update tb_interview set status = #{i} where id = #{interviewId} and user_id = #{userId}")
    int updateInterviewStatusById(Long userId, Long interviewId, int i);

    @Select("select id, team_job_id, user_id, begin_time, end_time, address, create_time, update_time, status from tb_interview limit #{offset}, #{pageSize}")
    List<Interview> queryInterviewAll(int offset, int pageSize);

    @Select("select count(*) from tb_interview")
    int queryInterviewTotal();

    @Select("SELECT " +
            "i.id, " +
            "i.team_job_id AS teamJobId, " +
            "i.user_id AS userId, " +
            "i.begin_time AS beginTime, " +
            "i.end_time AS endTime, " +
            "i.address, " +
            "i.create_time AS createTime, " +
            "i.update_time AS updateTime, " +
            "i.status, " +
            "u.username AS username, " +
            "t.username AS teamname, " +
            "j.name AS jobname " +
            "FROM tb_interview i " +
            "LEFT JOIN tb_user u ON i.user_id = u.id " +
            "LEFT JOIN tb_team_job tj ON i.team_job_id = tj.id " +
            "LEFT JOIN tb_team t ON tj.team_id = t.id " +
            "LEFT JOIN tb_job j ON tj.job_id = j.id " +
            "LIMIT #{offset}, #{pageSize}")
    List<interviewVO> queryInterviewVOAll(int offset, int pageSize);

    /*@Select("SELECT " +
            "i.id, " +
            "i.team_job_id AS teamJobId, " +
            "i.user_id AS userId, " +
            "i.begin_time AS beginTime, " +
            "i.end_time AS endTime, " +
            "i.address, " +
            "i.create_time AS createTime, " +
            "i.update_time AS updateTime, " +
            "i.status, " +
            "u.username AS username, " +
            "t.username AS teamname " +
            "FROM tb_interview i " +
            "LEFT JOIN tb_user u ON i.user_id = u.id " +
            "LEFT JOIN tb_team_job tj ON i.team_job_id = tj.id " +
            "LEFT JOIN tb_team t ON tj.team_id = t.id " +
            "LIMIT #{offset}, #{pageSize}")
    List<interviewVO> queryInterviewVOAll(int offset, int pageSize);*/

    @Select("SELECT COUNT(*) FROM tb_interview i " +
            "LEFT JOIN tb_user u ON i.user_id = u.id " +
            "LEFT JOIN tb_team_job tj ON i.team_job_id = tj.id " +
            "LEFT JOIN tb_team t ON tj.team_id = t.id")
    int queryInterviewVOTotal();


}
