package com.abc.xyzp.mapper;

import com.abc.xyzp.entity.Admin;
import com.abc.xyzp.entity.Team;
import com.abc.xyzp.entity.TeamJob;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMapper {
    @Select("select * from tb_admin where username = #{username} and status = 1")
    Admin queryAdminByUsername(String username);

    @Select("select id, username, email from tb_admin where id = #{adminId} and status = 1")
    Admin queryAdminById(Long adminId);

    @Select("select id, team_id from tb_team_job where tb_team_job.id = #{id}")
    TeamJob queryTeamJobById(Long id);

    @Update("update tb_team_job set status = #{i} where id = #{id} and team_id = #{teamId}")
    int updateTeamJobStatus(Long teamId, Long i, Long id);

    @Select("select id, username, email, create_time, update_time, status from tb_admin limit #{i}, #{pageSize}")
    List<Admin> queryAdminAll(int i, int pageSize);

    @Insert("insert into tb_admin (id, username, password, email) values (#{id}, #{username}, #{password}, #{email})")
    int save(Admin admin);

    @Select("select count(*) from tb_admin")
    int queryAdminTotal();

    @Select("select id, username, email, create_time, update_time, status from tb_admin where username like concat('%',#{content},'%')")
    List<Admin> queryAdminLikeByUsername(String content);

    @Update("update tb_admin set password = #{md5Hex} where id = #{id}")
    int updateAdminPasswordById(Long id, String md5Hex);

    @Update("update tb_admin set status = #{i} where id = #{id}")
    int updateAdminStatusById(Long id, int i);

    @Update("update tb_admin set email = #{email}, password = #{password} where username = #{username}")
    int updateAdmin(Admin admin);
}
