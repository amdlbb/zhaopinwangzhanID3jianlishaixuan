package com.abc.xyzp.controller;

import com.abc.xyzp.VO.DeliverVO;
import com.abc.xyzp.entity.Admin;
import com.abc.xyzp.entity.Result;
import com.abc.xyzp.mapper.AdminMapper;
import com.abc.xyzp.service.DeliverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deliver")
public class DeliverController {

    @Autowired
    private DeliverService deliverService;

    @Autowired
    private AdminMapper adminMapper;

    @GetMapping("/list")
    public Result getDeliverList() {
        List<DeliverVO> deliverList = deliverService.getAllDeliverList();
        return Result.success(deliverList);
    }

    /**
     * 搜索投递记录（基于 Elasticsearch）
     * 支持全文检索和高亮显示
     *
     * @param httpServletRequest HTTP 请求对象
     * @param data 包含搜索关键词的请求体，格式: {"content": "搜索关键词"}
     * @return 包含高亮信息的投递记录列表
     */
    @PostMapping("/searchResume")
    public Result<List<DeliverVO>> searchResume(HttpServletRequest httpServletRequest,
                                                @RequestBody Map<String, String> data) {
        Long adminId = Long.valueOf(String.valueOf(httpServletRequest.getAttribute("userId")));
        // 查询是否为管理员
        Admin findAdmin = adminMapper.queryAdminById(adminId);
        if(findAdmin == null){
            return Result.error("您的权限不够，只有管理员才能查询");
        }

        String keyword = String.valueOf(data.get("content"));
        List<DeliverVO> result = deliverService.searchResume(httpServletRequest,keyword);
        return Result.success(result);
    }
}