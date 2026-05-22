package com.abc.xyzp.service.impl;

import com.abc.xyzp.VO.DeliverVO;
import com.abc.xyzp.entity.Resume;
import com.abc.xyzp.entity.UserResume;
import com.abc.xyzp.service.DeliverEsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.elasticsearch.common.text.Text;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Elasticsearch 投递记录服务实现类
 * 负责将 DeliverVO 列表全量同步到 ES 索引
 */
@Slf4j
@Service
public class DeliverEsServiceImpl implements DeliverEsService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * ES 索引名称（与你创建的索引一致）
     */
    private static final String INDEX_NAME = "resume";

    /**
     * JSON 序列化工具，用于将 DeliverVO 转换为 JSON 字符串
     */
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }


    @Override
    public void syncDeliverListToEs(List<DeliverVO> deliverVOList) {
        // 参数校验：如果列表为空或null，直接返回
        if (deliverVOList == null || deliverVOList.isEmpty()) {
            log.warn("待同步的投递记录列表为空，跳过同步");
            return;
        }

        log.info("开始全量同步 {} 条投递记录到 Elasticsearch...", deliverVOList.size());

        try {
            // 1. 创建批量请求对象
            BulkRequest bulkRequest = new BulkRequest();

            // 2. 遍历每条投递记录，构建索引请求
            for (DeliverVO vo : deliverVOList) {
                // 2.1 将 DeliverVO 对象转换为 JSON 字符串
                //    由于 ES 索引结构与 DeliverVO 完全一致，可以直接序列化
                String jsonString = OBJECT_MAPPER.writeValueAsString(vo);

                // 2.2 创建索引请求
                //    - 指定索引名称为 "resume"
                //    - 使用投递记录ID作为文档ID（确保更新时覆盖旧文档）
                IndexRequest indexRequest = new IndexRequest(INDEX_NAME)
                        .id(String.valueOf(vo.getId()))  // 使用投递ID作为ES文档ID
                        .source(jsonString, XContentType.JSON);

                // 2.3 将索引请求添加到批量请求中
                bulkRequest.add(indexRequest);
            }

            // 3. 执行批量写入操作
            BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

            // 4. 检查是否有失败的请求
            if (bulkResponse.hasFailures()) {
                log.error("批量同步到ES失败: {}", bulkResponse.buildFailureMessage());
            } else {
                log.info("成功同步 {} 条投递记录到 Elasticsearch", deliverVOList.size());
            }

        } catch (IOException e) {
            log.error("同步投递记录到ES时发生IO异常", e);
        } catch (Exception e) {
            log.error("同步投递记录到ES时发生未知异常", e);
        }
    }

    @Override
    public List<DeliverVO> searchResume(String keyword) {
        log.info("开始搜索投递记录，关键词: {}", keyword);

        try {
            // 1. 构建搜索请求
            SearchRequest searchRequest = buildSearchRequest(keyword);

            // 2. 执行搜索
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            // 3. 解析搜索结果并封装高亮信息
            List<DeliverVO> result = parseSearchResponse(searchResponse);

            log.info("搜索完成，共找到 {} 条记录", result.size());

            // 4. 输出到控制台用于测试验证
            printSearchResultToConsole(result);

            return result;

        } catch (IOException e) {
            log.error("搜索投递记录时发生IO异常", e);
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("搜索投递记录时发生未知异常", e);
            return new ArrayList<>();
        }
    }


    /**
     * 构建 ES 搜索请求
     * 包含 match 查询和高亮配置
     *
     * @param keyword 搜索关键词
     * @return 配置好的 SearchRequest 对象
     */
    private SearchRequest buildSearchRequest(String keyword) {
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 1. 构建查询条件：在 all 字段中进行 match 查询，operator 为 AND
        sourceBuilder.query(
                QueryBuilders.matchQuery("all", keyword)
                        .operator(org.elasticsearch.index.query.Operator.AND)
        );

        // 2. 构建高亮配置
        HighlightBuilder highlightBuilder = new HighlightBuilder();

        // 2.1 设置高亮标签
        highlightBuilder.preTags("<span style='color:red;font-weight:bold'>");
        highlightBuilder.postTags("</span>");

        // 2.2 设置不需要字段匹配（允许跨字段高亮）
        highlightBuilder.requireFieldMatch(false);

        // 2.3 添加需要高亮的字段
        highlightBuilder.field("jobname");
        highlightBuilder.field("nickname");
        highlightBuilder.field("resume.major");
        highlightBuilder.field("teamname");
        highlightBuilder.field("userResume.content");
        highlightBuilder.field("userResume.exceptionJob");
        highlightBuilder.field("userResume.major");
        highlightBuilder.field("userResume.name");
        highlightBuilder.field("userResume.school");

        sourceBuilder.highlighter(highlightBuilder);

        // 3. 设置返回结果数量
        sourceBuilder.size(20);

        searchRequest.source(sourceBuilder);
        return searchRequest;
    }

    /**
     * 解析 ES 搜索响应，将高亮信息封装到 DeliverVO 中
     *
     * @param searchResponse ES 搜索响应
     * @return 包含高亮信息的 DeliverVO 列表
     */
    private List<DeliverVO> parseSearchResponse(SearchResponse searchResponse) {
        List<DeliverVO> result = new ArrayList<>();

        SearchHit[] hits = searchResponse.getHits().getHits();

        for (SearchHit hit : hits) {
            try {
                // 1. 解析 _source 为 DeliverVO 对象
                String sourceAsString = hit.getSourceAsString();
                DeliverVO vo = OBJECT_MAPPER.readValue(sourceAsString, DeliverVO.class);

                // 2. 获取高亮字段
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();

                // 3. 将高亮内容填充到对应的字段中
                fillHighlightFields(vo, highlightFields);

                result.add(vo);

            } catch (IOException e) {
                log.error("解析搜索结果时发生错误", e);
            }
        }

        return result;
    }

    /**
     * 将高亮字段填充到 DeliverVO 对象中
     *
     * @param vo 投递记录 VO 对象
     * @param highlightFields 高亮字段映射
     */
    private void fillHighlightFields(DeliverVO vo, Map<String, HighlightField> highlightFields) {
        // 1. 处理 jobname 高亮，如果存在则替换 jobname
        if (highlightFields.containsKey("jobname")) {
            Text[] fragments = highlightFields.get("jobname").fragments();
            if (fragments.length > 0) {
                vo.setJobname(fragments[0].string());
            }
        }

        // 2. 处理 nickname 高亮，如果存在则替换 nickname
        if (highlightFields.containsKey("nickname")) {
            Text[] fragments = highlightFields.get("nickname").fragments();
            if (fragments.length > 0) {
                vo.setNickname(fragments[0].string());
            }
        }

        // 3. 处理 teamname 高亮，如果存在则替换 teamname
        if (highlightFields.containsKey("teamname")) {
            Text[] fragments = highlightFields.get("teamname").fragments();
            if (fragments.length > 0) {
                vo.setTeamname(fragments[0].string());
            }
        }

        // 4. 处理 userResume 中的高亮字段，填充到 userResume 中
        if (vo.getUserResume() != null) {
            UserResume userResume = vo.getUserResume();

            // 4.1 userResume.name
            if (highlightFields.containsKey("userResume.name")) {
                Text[] fragments = highlightFields.get("userResume.name").fragments();
                if (fragments.length > 0) {
                    userResume.setName(fragments[0].string());
                }
            }

            // 4.2 userResume.major
            if (highlightFields.containsKey("userResume.major")) {
                Text[] fragments = highlightFields.get("userResume.major").fragments();
                if (fragments.length > 0) {
                    userResume.setMajor(fragments[0].string());
                }
            }

            // 4.3 userResume.school
            if (highlightFields.containsKey("userResume.school")) {
                Text[] fragments = highlightFields.get("userResume.school").fragments();
                if (fragments.length > 0) {
                    userResume.setSchool(fragments[0].string());
                }
            }

            // 4.4 userResume.content
            if (highlightFields.containsKey("userResume.content")) {
                Text[] fragments = highlightFields.get("userResume.content").fragments();
                if (fragments.length > 0) {
                    userResume.setContent(fragments[0].string());
                }
            }

            // 4.5 userResume.exceptionJob
            if (highlightFields.containsKey("userResume.exceptionJob")) {
                Text[] fragments = highlightFields.get("userResume.exceptionJob").fragments();
                if (fragments.length > 0) {
                    userResume.setExceptionJob(fragments[0].string());
                }
            }
        }

        // 5. 处理 resume.major 高亮
        if (vo.getResume() != null && highlightFields.containsKey("resume.major")) {
            Text[] fragments = highlightFields.get("resume.major").fragments();
            if (fragments.length > 0) {
                vo.getResume().setMajor(fragments[0].string());
            }
        }
    }

    /**
     * 将搜索结果输出到控制台，用于测试验证
     *
     * @param result 搜索结果列表
     */
    private void printSearchResultToConsole(List<DeliverVO> result) {
        System.out.println("========== ES 搜索结果 ==========");
        System.out.println("共找到 " + result.size() + " 条记录");
        System.out.println();

        for (int i = 0; i < result.size(); i++) {
            DeliverVO vo = result.get(i);
            System.out.println("--- 记录 " + (i + 1) + " ---");
            System.out.println("ID: " + vo.getId());
            System.out.println("用户名: " + vo.getUsername());
            System.out.println("昵称: " + vo.getNickname());
            System.out.println("职位: " + vo.getJobname());
            System.out.println("团队: " + vo.getTeamname());
            System.out.println("预测结果: " + vo.getPredictResult());

            if (vo.getUserResume() != null) {
                UserResume ur = vo.getUserResume();
                System.out.println("简历姓名: " + ur.getName());
                System.out.println("简历专业: " + ur.getMajor());
                System.out.println("学校: " + ur.getSchool());
                System.out.println("期望职位: " + ur.getExceptionJob());
            }

            if (vo.getResume() != null) {
                System.out.println("简化简历专业: " + vo.getResume().getMajor());
            }

            System.out.println();
        }

        System.out.println("=================================");
    }

}