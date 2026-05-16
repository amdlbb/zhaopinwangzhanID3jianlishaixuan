package com.abc.xyzp.mapper;

import com.abc.xyzp.entity.Examine;
import com.abc.xyzp.entity.Team;
import com.abc.xyzp.entity.TeamJob;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ExamineMapper {

    @Insert("insert into tb_examine (id, team_job_id, admin_id) value (#{id}, #{teamJobId}, #{adminId})")
    int save(Examine examine);

    @Select("select id, team_job_id, create_time from tb_examine where result = 0 limit #{i}, #{pageSize}")
    List<Examine> queryExamineAll(int i, int pageSize);

    @Select("select count(id) from tb_examine where result = 0")
    int queryExamineTotal();

    @Update("update tb_examine set result = #{result} where id = #{examineId}")
    int updateResultById(Long examineId, int result);

    @Select("select id, team_job_id, result from tb_examine where status = 1 and id = #{examineId}")
    Examine queryExamineById(Long examineId);

    @Update("update tb_examine set result = #{status} where team_job_id = #{teamJobId}")
    int updateResultByTeamJobId(Long teamJobId, Long status);

    @Select("select tb_team.id teamId, tb_team_job.id teamJobId, tb_examine.id, tb_team_job.address, tb_team_job.content, tb_team_job.education, tb_team_job.salary, tb_team_job.number, tb_job.name, tb_team.nickname from tb_examine, tb_job, tb_team_job, tb_team " +
            "where result = 0 and tb_examine.team_job_id = tb_team_job.id and tb_team_job.team_id = tb_team.id and tb_team_job.job_id = tb_job.id limit #{i}, #{pageSize}")
    List<Map<String, Object>> queryExamineAllByAdmin(int i, int pageSize);

    @Update("update tb_examine set result = #{status}, admin_id = #{adminId} where id = #{id}")
    int updateResultStatusById(long id, int status, Long adminId);

    @Select("select tb_team.id teamId, tb_team_job.id teamJobId, tb_examine.id, tb_team_job.address, tb_team_job.content, tb_team_job.education, tb_team_job.salary, tb_team_job.number, tb_job.name, tb_team.nickname from tb_examine, tb_job, tb_team_job, tb_team " +
            "where result = 0 and tb_examine.team_job_id = tb_team_job.id and tb_team_job.team_id = tb_team.id and tb_team_job.job_id = tb_job.id and tb_team.nickname like concat('%',#{content},'%')")
    List<TeamJob> searchAdminExamineListByContent(String content);
}
