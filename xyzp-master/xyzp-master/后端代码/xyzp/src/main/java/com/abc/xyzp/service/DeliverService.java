package com.abc.xyzp.service;

import com.abc.xyzp.VO.DeliverVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface DeliverService {

    List<DeliverVO> getAllDeliverList();

    /**
     * 搜索投递记录（基于 Elasticsearch）
     * 支持全文检索和高亮显示
     *
     * @param httpServletRequest
     * @param keyword            搜索关键词
     * @return 包含高亮信息的投递记录列表
     */
    List<DeliverVO> searchResume(HttpServletRequest httpServletRequest, String keyword);
}