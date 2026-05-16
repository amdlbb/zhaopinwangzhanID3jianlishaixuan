package com.abc.xyzp.mapper;

import com.abc.xyzp.entity.Job;
import com.abc.xyzp.entity.Result;
import com.abc.xyzp.entity.Team;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobMapper {

    @Select("select id, name, p_id from tb_job where status = 1")
    List<Job> getAll();

    @Select("select count(*) from tb_job where status = 1 and p_id is not null")
    int queryJobTotal();

    @Select("select j1.id, j1.name as name, j1.create_time, j1.update_time, j2.name as pJobName, j1.status from tb_job j1, tb_job j2 where j1.p_id is not null and j1.p_id = j2.id limit #{i}, #{pageSize}")
    List<Job> queryJobTotalByAdmin(int i, int pageSize);

    @Select("select id, name from tb_job where p_id = 0 and status = 1")
    List<Job> queryJobCategory1();

    @Select("select j1.id, j1.name from tb_job j1, tb_job j2 where j2.p_id = 0 and j1.status = 1 and j1.p_id = j2.id")
    List<Job> queryJobCategory2();

    @Select("select j1.id, j1.name from tb_job j1, tb_job j2, tb_job j3 where j2.p_id = j3.id and j1.status = 1 and j1.p_id = j2.id and j3.p_id = 0")
    List<Job> queryJobCategory3();

    @Update("update tb_job set name = #{name} where id = ${id}")
    int updateJobById(Job job);

    @Select("select j1.id, j1.status, j1.name as name, j1.create_time, j1.update_time, j2.name as pJobName from tb_job j1, tb_job j2 where j1.p_id is not null and j1.p_id = j2.id and j1.name like concat('%',#{content},'%')")
    List<Job> queryNameByContent(String content);

    @Update("update tb_job set status = #{i} where id = ${id}")
    int updateJobStatusById(Long id, int i);


    @Insert("insert into tb_job (id, name, p_id) values (#{id}, #{name}, #{pId})")
    int save(String name, Long pId, Long id);

    @Select("select count(*) from tb_job j1, tb_job j2 where j1.p_id is not null and j1.p_id = j2.id")
    int queryJobTotalNumByAdmin();

    @Select("select count(*) from tb_job where status = 1")
    Integer getTotalNumTest();

    @Select("select * from tb_job where id = #{id} and status = 1")
    Job queryJobById(Long id);
}
