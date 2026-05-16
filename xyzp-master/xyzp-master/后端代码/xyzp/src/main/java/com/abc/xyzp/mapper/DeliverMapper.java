package com.abc.xyzp.mapper;

import com.abc.xyzp.VO.DeliverVO;
import com.abc.xyzp.entity.Deliver;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliverMapper {
    @Update("update tb_deliver set status = #{status} where tb_deliver.user_id = #{userId} and tb_deliver.id = #{deliverId}")
    int updateUserDeliverStatusById(Long userId, Long deliverId, int status);

    @Insert("insert into tb_deliver (id, team_job_id, user_id, status) values (#{id}, #{teamJobId}, #{userId}, #{status})")
    int save(Deliver deliver);

    @Select("select * from tb_deliver where user_id = #{userId} and team_job_id = #{data} and status = 1")
    Deliver queryDeliverByUserIdAndTeamJobId(Long userId, Long data);

    @Select("select * from tb_deliver where status = 1 order by create_time desc")
    List<Deliver> queryAllActiveDelivers();
}
