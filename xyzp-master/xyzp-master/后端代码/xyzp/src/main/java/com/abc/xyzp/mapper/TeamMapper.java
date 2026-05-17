package com.abc.xyzp.mapper;

import com.abc.xyzp.dto.AdmissionDto;
import com.abc.xyzp.dto.InterviewDto;
import com.abc.xyzp.dto.UserResumeDto;
import com.abc.xyzp.entity.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMapper {

    @Insert("insert into tb_team (id, username, password, nickname, introduce, avatar, email) values" +
            " (#{id}, #{username}, #{password}, #{nickname}, #{introduce}, #{avatar}, #{email})")
    int save(User user);

    @Select("select * from tb_team where username = #{username} and status = 1")
    Team queryUserByUsername(String username);

    @Select("select * from tb_team where id = #{id}")
    Team queryUserById(Long id);

    @Update("update tb_team set nickname = #{nickname}, introduce = #{introduce}, avatar = #{avatar}, address = #{address} where id = #{id}")
    int updateById(Team team);

    @Update("update tb_team set password = #{password} where id = #{userId}")
    int updatePasswordById(Long userId, String password);

    @Insert("insert into tb_team_job (id, team_id, job_id, salary, number, address, status, education, content) value (" +
            "#{id}, #{teamId}, #{jobId}, #{salary}, #{number}, #{address}, #{status}, #{education}, #{content})")
    int saveTeamJob(TeamJob teamJob);

    @Select("select name, number, address, salary, education, content, tb_team_job.status, tb_team_job.id from tb_team_job, tb_job where team_id = #{teamId} and tb_team_job.job_id = tb_job.id limit #{i}, #{pageSize}")
    List<TeamJob> queryTeamJobListById(Long teamId, int i, int pageSize);

    @Select("select tb_team_job.id, name, nickname, salary, number, avatar, education from tb_job, tb_team_job, tb_team where tb_team_job.team_id = tb_team.id and tb_team_job.job_id = tb_job.id and tb_team_job.status = 4 " +
            "order by tb_team_job.create_time desc limit #{page}, #{pageSize}")
    List<TeamJob> queryNewTeamJob(int page, int pageSize);

    @Select("select tb_team.id, name, nickname, salary, number, avatar, education, introduce, content, tb_team_job.address from tb_job, tb_team_job, tb_team where tb_team_job.id = #{id} and tb_team_job.status = 4 and tb_team_job.team_id = tb_team.id and tb_team_job.job_id = tb_job.id")
    TeamJob queryTeamJobById(Long id);

    @Update("update tb_team_job set address = #{address}, number = #{number}, salary = #{salary}, education = #{education}, content = #{content} where id = #{id}")
    int updateTeamJob(TeamJob teamJob);

    @Select("select count(id) from tb_team_job where team_id = #{teamId} and id = #{id}")
    int queryCountTeamJobNum(Long teamId, Long id);

    @Select("select count(id) from tb_team_job where status = 4")
    int queryNewTeamJobTotal();

    @Select("select count(id) from tb_team_job where team_id = #{teamId}")
    int queryTeamJobTotalById(Long teamId);

    @Select("select tb_deliver.id deliverId, tb_team_job.id teamJobId, tb_user.id userId, tb_user_resume.age, tb_user_resume.content, tb_user_resume.phone, tb_job.name jobName, tb_user_resume.name name, tb_user_resume.sex, tb_user_resume.education, tb_user_resume.school, tb_user_resume.exception_salary, tb_user_resume.exception_job from tb_user_resume, tb_deliver, tb_user, tb_team_job, tb_job where " +
            "tb_deliver.user_id = tb_user.id and tb_user.user_resume_id = tb_user_resume.id and tb_deliver.team_job_id = tb_team_job.id and tb_team_job.team_id = #{teamId} and tb_team_job.job_id = tb_job.id and tb_deliver.status = 1 limit #{i}, #{pageSize}")
    List<UserResumeDto> queryUserResumeById(Long teamId, int i, int pageSize);

    @Select("select count(*) from tb_user_resume, tb_deliver, tb_user, tb_team_job, tb_job where " +
            "tb_deliver.user_id = tb_user.id and tb_user.user_resume_id = tb_user_resume.id and tb_deliver.team_job_id = tb_team_job.id and tb_team_job.team_id = #{teamId} and tb_team_job.job_id = tb_job.id and tb_deliver.status = 1")
    int queryUserResumeTotalById(Long teamId);

    @Update("update tb_team_job set status = #{i} where id = #{id} and team_id = #{teamId}")
    int updateTeamJobStatus(Long teamId, Long i, Long id);

    @Select("select name, number, address, salary, education, content, tb_team_job.status, tb_team_job.id from tb_team_job, tb_job where team_id = #{teamId} and tb_team_job.job_id = tb_job.id and tb_job.name like concat('%',#{content},'%')")
    List<TeamJob> searchTeamJobListById(Long teamId, String content);

    @Select("select count(*) from tb_team_job, tb_job where team_id = #{teamId} and tb_team_job.job_id = tb_job.id and tb_job.name like concat('%',#{content},'%')")
    int searchTeamJobTotalById(Long teamId, String content);

    @Select("select tb_deliver.id deliverId, tb_team_job.id teamJobId, tb_user.id userId, tb_user_resume.age, tb_user_resume.content, tb_user_resume.phone, tb_job.name jobName, tb_user_resume.name name, tb_user_resume.sex, tb_user_resume.education, tb_user_resume.school, tb_user_resume.exception_salary, tb_user_resume.exception_job from tb_user_resume, tb_deliver, tb_user, tb_team_job, tb_job where " +
            "tb_deliver.user_id = tb_user.id and tb_user.user_resume_id = tb_user_resume.id and tb_deliver.team_job_id = tb_team_job.id and tb_team_job.team_id = #{teamId} and tb_team_job.job_id = tb_job.id and tb_deliver.status = 1 and tb_job.name like concat('%', #{content},'%')")
    List<UserResumeDto> searchUserResumeById(Long teamId, String content);

    @Select("select count(*) from tb_user_resume, tb_deliver, tb_user, tb_team_job, tb_job where " +
            "tb_deliver.user_id = tb_user.id and tb_user.user_resume_id = tb_user_resume.id and tb_deliver.team_job_id = tb_team_job.id and tb_team_job.team_id = #{teamId} and tb_team_job.job_id = tb_job.id and tb_deliver.status = 1 and tb_job.name like concat('%', #{content}, '%')")
    int searchUserResumeTotalById(Long teamId, String content);

    @Select("select tb_interview.id interviewId, tb_interview.address, tb_interview.begin_time beginTime, tb_interview.end_time endTime, tb_team_job.id teamJobId, tb_user.id userId, tb_user_resume.age, tb_user_resume.content, tb_user_resume.phone, tb_job.name jobName, tb_user_resume.name name, tb_user_resume.sex, tb_user_resume.education, tb_user_resume.school, tb_user_resume.exception_salary, tb_user_resume.exception_job from tb_interview, tb_team_job, tb_user, tb_user_resume, tb_team, tb_job " +
            "where tb_interview.team_job_id = tb_team_job.id and tb_interview.user_id = tb_user.id and tb_user.user_resume_id = tb_user_resume.id and tb_team.id = #{teamId} and tb_team.id = tb_team_job.team_id and tb_team_job.job_id = tb_job.id and tb_interview.status = 1 limit #{i}, #{pageSize}")
    List<InterviewDto> queryInterviewById(Long teamId, int i, int pageSize);

    @Select("select count(*) from tb_interview, tb_team_job, tb_user, tb_user_resume, tb_team, tb_job " +
            "where tb_interview.team_job_id = tb_team_job.id and tb_interview.user_id = tb_user.id and tb_user.user_resume_id = tb_user_resume.id and tb_team.id = #{teamId} and tb_team.id = tb_team_job.team_id and tb_team_job.job_id = tb_job.id and tb_interview.status = 1")
    int queryInterviewTotalById(Long teamId);

    @Select("select tb_interview.id interviewId, tb_interview.address, tb_interview.begin_time beginTime, tb_interview.end_time endTime, tb_team_job.id teamJobId, tb_user.id userId, tb_user_resume.age, tb_user_resume.content, tb_user_resume.phone, tb_job.name jobName, tb_user_resume.name name, tb_user_resume.sex, tb_user_resume.education, tb_user_resume.school, tb_user_resume.exception_salary, tb_user_resume.exception_job from tb_interview, tb_team_job, tb_user, tb_user_resume, tb_team, tb_job " +
            "where tb_interview.team_job_id = tb_team_job.id and tb_interview.user_id = tb_user.id and tb_user.user_resume_id = tb_user_resume.id and tb_team.id = #{teamId} and tb_team.id = tb_team_job.team_id and tb_team_job.job_id = tb_job.id and tb_interview.status = 1 and tb_job.name like concat('%', #{content}, '%')")
    List<InterviewDto> searchInterviewById(Long teamId, String content);

    @Select("select count(*) from tb_interview, tb_team_job, tb_user, tb_user_resume, tb_team, tb_job " +
            "where tb_interview.team_job_id = tb_team_job.id and tb_interview.user_id = tb_user.id and tb_user.user_resume_id = tb_user_resume.id and tb_team.id = #{teamId} and tb_team.id = tb_team_job.team_id and tb_team_job.job_id = tb_job.id and tb_interview.status = 1 and tb_job.name like concat('%', #{content}, '%')")
    int searchInterviewTotalById(Long teamId, String content);

    @Select("select tb_admission.id, tb_admission.work_address workAddress, tb_admission.work_time workTime, tb_team_job.id teamJobId, tb_user.id userId, tb_user.email email, tb_user.username username, tb_user_resume.age, tb_user_resume.content, tb_user_resume.phone, tb_job.name jobName, tb_user_resume.name name, tb_user_resume.sex, tb_user_resume.education, tb_user_resume.school, tb_user_resume.exception_salary, tb_user_resume.exception_job from tb_admission, tb_team_job, tb_user, tb_user_resume, tb_team, tb_job " +
            "where tb_admission.team_job_id = tb_team_job.id and tb_admission.user_id = tb_user.id and tb_user.user_resume_id = tb_user_resume.id and tb_team.id = tb_team_job.team_id and tb_team_job.job_id = tb_job.id and tb_admission.status = 1 limit #{i}, #{pageSize}")
    List<AdmissionDto> queryAllAdmission(int i, int pageSize);

    @Select("select count(*) from tb_admission, tb_team_job, tb_user, tb_user_resume, tb_team, tb_job " +
            "where tb_admission.team_job_id = tb_team_job.id and tb_admission.user_id = tb_user.id and tb_user.user_resume_id = tb_user_resume.id and tb_team.id = tb_team_job.team_id and tb_team_job.job_id = tb_job.id and tb_admission.status = 1")
    int queryAllAdmissionTotal();


    @Select("select tb_admission.id, tb_admission.work_address workAddress, tb_admission.work_time workTime, tb_team_job.id teamJobId, tb_user.id userId, tb_user_resume.age, tb_user_resume.content, tb_user_resume.phone, tb_job.name jobName, tb_user_resume.name name, tb_user_resume.sex, tb_user_resume.education, tb_user_resume.school, tb_user_resume.exception_salary, tb_user_resume.exception_job from tb_admission, tb_team_job, tb_user, tb_user_resume, tb_team, tb_job " +
            "where tb_admission.team_job_id = tb_team_job.id and tb_admission.user_id = tb_user.id and tb_user.user_resume_id = tb_user_resume.id and tb_team.id = #{teamId} and tb_team.id = tb_team_job.team_id and tb_team_job.job_id = tb_job.id and tb_admission.status = 1 limit #{i}, #{pageSize}")
    List<AdmissionDto> queryAdmissionById(Long teamId, int i, int pageSize);

    @Select("select count(*) from tb_admission, tb_team_job, tb_user, tb_user_resume, tb_team, tb_job " +
            "where tb_admission.team_job_id = tb_team_job.id and tb_admission.user_id = tb_user.id and tb_user.user_resume_id = tb_user_resume.id and tb_team.id = #{teamId} and tb_team.id = tb_team_job.team_id and tb_team_job.job_id = tb_job.id and tb_admission.status = 1")
    int queryAdmissionTotalById(Long teamId);

    @Select("select tb_admission.id, tb_admission.work_address workAddress, tb_admission.work_time workTime, tb_team_job.id teamJobId, tb_user.id userId, tb_user_resume.age, tb_user_resume.content, tb_user_resume.phone, tb_job.name jobName, tb_user_resume.name name, tb_user_resume.sex, tb_user_resume.education, tb_user_resume.school, tb_user_resume.exception_salary, tb_user_resume.exception_job from tb_admission, tb_team_job, tb_user, tb_user_resume, tb_team, tb_job " +
            "where tb_admission.team_job_id = tb_team_job.id and tb_admission.user_id = tb_user.id and tb_user.user_resume_id = tb_user_resume.id and tb_team.id = #{teamId} and tb_team.id = tb_team_job.team_id and tb_team_job.job_id = tb_job.id and tb_admission.status = 1 and tb_job.name like concat('%', #{content}, '%')")
    List<AdmissionDto> searchAdmissionById(Long teamId, String content);

    @Select("select count(*) from tb_admission, tb_team_job, tb_user, tb_user_resume, tb_team, tb_job " +
            "where tb_admission.team_job_id = tb_team_job.id and tb_admission.user_id = tb_user.id and tb_user.user_resume_id = tb_user_resume.id and tb_team.id = #{teamId} and tb_team.id = tb_team_job.team_id and tb_team_job.job_id = tb_job.id and tb_admission.status = 1 and tb_job.name like concat('%', #{content}, '%')")
    int searchAdmissionTotalById(Long teamId, String content);

    @Select("select tb_team_job.id, name, nickname, salary, number, avatar, education from tb_job, tb_team_job, tb_team where tb_team_job.team_id = tb_team.id and tb_team_job.job_id = tb_job.id and tb_team_job.status = 4 order by rand() limit 0, 5")
    List<TeamJob> queryRecommendTeamJob();

    @Select("select tb_team_job.id, name, nickname, salary, number, avatar, education from tb_job, tb_team_job, tb_team where tb_team_job.team_id = tb_team.id and tb_team_job.job_id = tb_job.id and tb_team_job.status = 4 and tb_job.name like concat('%',#{content},'%') " +
            "order by tb_team_job.create_time desc")
    List<TeamJob> searchTeamJobListByContent(String content);

    @Select("select id, username, nickname, address, avatar, introduce, email, create_time, update_time, status from tb_team limit #{i}, #{pageSize}")
    List<Team> queryTeamAll(int i, int pageSize);

    @Select("select count(id) from tb_team where status = 1")
    int queryTeamTotal();

    @Select("select id, username, nickname, address, avatar, introduce, email, create_time, update_time, status from tb_team where username like concat('%',#{content},'%')")
    List<Team> queryTeamByUsername(String content);

    @Update("update tb_team set status = #{i} where id = #{id}")
    int updateTeamStatusById(Long id, int i);

    @Update("update tb_team set password = #{md5Hex} where id = #{id}")
    int updateTeamPasswordById(Long id, String md5Hex);

    @Select("select tb_team_job.id, name, nickname, salary, number, avatar, education from tb_job, tb_team_job, tb_team where tb_team_job.team_id = tb_team.id and tb_team_job.job_id = tb_job.id and tb_team_job.status = 4 and tb_job.name like concat('%',#{content},'%') " +
            "order by tb_team_job.create_time desc")
    List<TeamJob> searchAdminTeamJobListByContent(String content);



    /**
     * 扣减招聘职位数量，如果数量减到0或以下，可选地直接更新状态（或者在Service层判断）
     * 这里我们只负责扣减数量
     */
    @Update("update tb_team_job set number = number - 1 where id = #{teamJobId} and number > 0")
    int decreaseJobNumber(Long teamJobId);

    /**
     * 查询当前职位的剩余数量
     */
    @Select("select number from tb_team_job where id = #{teamJobId}")
    Integer getJobNumberById(Long teamJobId);

}

