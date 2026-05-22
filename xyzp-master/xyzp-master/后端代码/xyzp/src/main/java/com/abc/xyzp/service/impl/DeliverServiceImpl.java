package com.abc.xyzp.service.impl;

import com.abc.xyzp.VO.DeliverVO;
import com.abc.xyzp.entity.*;
import com.abc.xyzp.mapper.*;
import com.abc.xyzp.service.DeliverEsService;
import com.abc.xyzp.service.DeliverService;
import com.abc.xyzp.service.ID3DecisionTree;
import com.abc.xyzp.service.ModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeliverServiceImpl implements DeliverService {

    @Autowired
    private DeliverMapper deliverMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private ID3DecisionTreeImpl id3DecisionTree;

    @Autowired
    private ModelService modelService;

    @Autowired
    private DeliverEsService deliverEsService;

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public List<DeliverVO> getAllDeliverList() {
        List<DeliverVO> deliverVOList = new ArrayList<>();

        List<Deliver> delivers = deliverMapper.queryAllActiveDelivers();

        if (delivers == null || delivers.isEmpty()) {
            return deliverVOList;
        }

        TreeNode model = modelService.loadModel();

        for (Deliver deliver : delivers) {
            DeliverVO vo = new DeliverVO();

            vo.setId(deliver.getId());
            vo.setTeamJobId(deliver.getTeamJobId());
            vo.setUserId(deliver.getUserId());
            vo.setCreateTime(deliver.getCreateTime());
            vo.setUpdateTime(deliver.getUpdateTime());

            User user = userMapper.queryUserById(deliver.getUserId());
            if (user != null) {
                vo.setUsername(user.getUsername());
                vo.setNickname(user.getNickname());

                UserResume userResume = userMapper.queryUserResumeById(user.getUserResumeId());
                if (userResume != null) {
                    vo.setUserResume(userResume);

                    Resume resume = convertToResume(userResume);
                    vo.setResume(resume);

                    if (model != null && resume != null) {
                        Boolean predictResult = id3DecisionTree.predict(model, resume);
                        vo.setPredictResult(predictResult != null ? predictResult : false);
                    } else {
                        vo.setPredictResult(false);
                    }
                }
            }

            TeamJob teamJob = teamMapper.queryTeamJobById(deliver.getTeamJobId());
            if (teamJob != null) {
                vo.setTeamId(teamJob.getTeamId());
                vo.setJobname(teamJob.getName());
                vo.setTeamname(teamJob.getNickname());
                log.info("{} {} {}",
                        teamJob.getNickname(),
                        teamJob.getName(),
                        teamJob.getTeamId());
            }

            deliverVOList.add(vo);
        }

        List<DeliverVO> sortedList = deliverVOList.stream()
                .sorted((v1, v2) -> {
                    Boolean r1 = v1.getPredictResult();
                    Boolean r2 = v2.getPredictResult();
                    if (r1.equals(r2)) {
                        return v2.getCreateTime().compareTo(v1.getCreateTime());
                    }
                    return r1 ? -1 : 1;
                })
                .collect(Collectors.toList());

        //全量更新索引库
        deliverEsService.syncDeliverListToEs(sortedList);

        return sortedList;
    }

    /**
     * 搜索投递记录（基于 Elasticsearch）
     * 支持全文检索和高亮显示
     *
     * @param keyword 搜索关键词
     * @return 包含高亮信息的投递记录列表
     */
    @Override
    public List<DeliverVO> searchResume(HttpServletRequest httpServletRequest, String keyword) {
        return deliverEsService.searchResume(keyword);
    }

    public Resume convertToResume(UserResume userResume) {
        if (userResume == null) {
            return null;
        }

        Resume resume = new Resume();
        resume.setId(userResume.getId());
        resume.setAge(userResume.getAge());
        resume.setEducation(userResume.getEducation());
        resume.setMajor(userResume.getMajor());

        try {
            if (userResume.getExceptionSalary() != null && !userResume.getExceptionSalary().isEmpty()) {
                resume.setExceptionSalary(new BigDecimal(userResume.getExceptionSalary()));
            }
        } catch (NumberFormatException e) {
            resume.setExceptionSalary(BigDecimal.ZERO);
        }

        resume.setAdmit(null);

        return resume;
    }
}
