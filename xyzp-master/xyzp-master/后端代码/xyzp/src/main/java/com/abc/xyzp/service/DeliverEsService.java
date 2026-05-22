package com.abc.xyzp.service;

import com.abc.xyzp.VO.DeliverVO;

import java.util.List;

/**
 * Elasticsearch 投递记录服务接口
 * 提供基于 ES 的数据同步和查询功能
 */
public interface DeliverEsService {

    /**
     * 全量同步投递记录列表到 Elasticsearch 索引
     * 接收即将返回给前端的 DeliverVO 列表，将其批量写入 ES 的 resume 索引
     *
     * @param deliverVOList 即将返回给前端的投递记录列表
     */
    void syncDeliverListToEs(List<DeliverVO> deliverVOList);

    /**
     * 根据关键词搜索投递记录（支持高亮显示）
     * 使用 ES 的 match 查询和 highlight 功能，在多个字段中搜索关键词并返回高亮结果
     *
     * @param keyword 搜索关键词
     * @return 包含高亮信息的投递记录列表
     */
    List<DeliverVO> searchResume(String keyword);
}



