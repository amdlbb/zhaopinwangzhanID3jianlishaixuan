package com.abc.xyzp.mapper;

import com.abc.xyzp.entity.User;
import com.abc.xyzp.entity.UserDeliver;
import com.abc.xyzp.entity.UserResume;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {

    @Select("select * from tb_user where username = #{username} and status = 1")
    User queryUserByUsername(String username);

    @Insert("insert into tb_user (id, username, password, nickname, introduce, avatar, email, user_resume_id) values" +
            " (#{id}, #{username}, #{password}, #{nickname}, #{introduce}, #{avatar}, #{email}, #{userResumeId})")
    int save(User user);

    @Update("update tb_user set nickname = #{nickname}, introduce = #{introduce}, avatar = #{avatar} where id = #{id}")
    int updateById(User user);

    @Select("select * from tb_user where id = #{id}")
    User queryUserById(Long id);

    @Select("select * from tb_user_resume where id = #{userResumeId}")
    UserResume queryUserResumeById(Long userResumeId);

    @Update("update tb_user set password = #{nowPassword} where id = #{userId}")
    int updatePasswordById(Long userId, String nowPassword);

    @Update("update tb_user_resume set name = #{name}, age = #{age}, sex = #{sex}, phone = #{phone}, major = #{major}, school = #{school}," +
            "education = #{education}, exception_job = #{exceptionJob}, exception_salary = #{exceptionSalary}, content = #{content} where id = #{id}")
    int updateUserResumeById(UserResume userResume);

    @Select("select tb_deliver.id, tb_team_job.salary, tb_job.name as jobName, tb_team.nickname as teamName, tb_team_job.address, tb_deliver.status from tb_deliver, tb_job, tb_team_job, tb_team " +
            "where tb_deliver.user_id = #{userId} and tb_deliver.team_job_id = tb_team_job.id and tb_team.id = tb_team_job.team_id and tb_team_job.job_id = tb_job.id and tb_deliver.status = 1 limit #{i}, #{pageSize}")
    List<UserDeliver> queryUserDeliverById(Long userId, int i, int pageSize);

    @Select("select tb_deliver.id, tb_team_job.salary, tb_job.name as jobName, tb_team.nickname as teamName, tb_team_job.address, tb_deliver.status from tb_deliver, tb_job, tb_team_job, tb_team " +
            "where tb_deliver.user_id = #{userId} and tb_deliver.team_job_id = tb_team_job.id and tb_team.id = tb_team_job.team_id and tb_team_job.job_id = tb_job.id and tb_deliver.status = 1 and tb_job.name like concat('%',#{content},'%')")
    List<UserDeliver> queryUserDeliverByContent(String content, Long userId);

    @Select("select count(id) from tb_deliver where tb_deliver.user_id = #{userId} and status = 1")
    int queryUserDeliverTotalById(Long userId);

    @Select("select id, username, nickname, avatar, email, create_time, update_time, introduce, status from tb_user limit #{i}, #{pageSize}")
    List<User> queryUserAll(int i, int pageSize);

    @Select("select count(id) from tb_user where status = 1")
    int queryUserTotal();

    @Select("select id, username, nickname, avatar, email, create_time, update_time, introduce, status  from tb_user where username like concat('%',#{content},'%')")
    List<User> queryUserLikeByUsername(String content);

    @Update("update tb_user set status = #{i} where id = #{id}")
    int updateUserStatusById(Long id, int i);

    @Select("select tb_admission.work_time workTime, tb_admission.work_address address, tb_team_job.salary, tb_job.name jobName, tb_team.nickname teamName from tb_admission, tb_team_job, tb_job, tb_team" +
            " where tb_admission.user_id = #{userId} and tb_admission.team_job_id = tb_team_job.id and tb_team_job.team_id = tb_team.id and tb_team_job.job_id = tb_job.id")
    List<Map<String, Object>> queryUserAdmissionByUserId(Long userId);

    @Select("select tb_admission.work_time workTime, tb_admission.work_address address, tb_team_job.salary, tb_job.name jobName, tb_team.nickname teamName from tb_admission, tb_team_job, tb_job, tb_team" +
            " where tb_admission.user_id = #{userId} and tb_admission.team_job_id = tb_team_job.id and tb_team_job.team_id = tb_team.id and tb_team_job.job_id = tb_job.id and tb_job.name like concat('%',#{content},'%')")
    List<Map<String, Object>> queryUserAdmissionByContent(Long userId, String content);

//    @Select("select * from tb_user, tb_user_resume where tb_user.id = #{id} and tb_user.user_resume_id = tb_user_resume.id")


}
