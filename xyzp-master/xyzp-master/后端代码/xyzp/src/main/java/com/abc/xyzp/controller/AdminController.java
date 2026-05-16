package com.abc.xyzp.controller;

import com.abc.xyzp.dto.AdminDto;
import com.abc.xyzp.entity.Admin;
import com.abc.xyzp.entity.Result;
import com.abc.xyzp.service.AdminService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private AdminService adminService;


    /**
     * 获取管理员信息
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/getAdminInfo")
    public Result<AdminDto> getAdminInfo(HttpServletRequest httpServletRequest){
        return adminService.getAdminInfo(httpServletRequest);
    }

    /**
     * 修改审核结果
     * @param httpServletRequest
     * @return
     */
    @PutMapping("/updateExamineResult")
    public Result<String> updateExamineResult(HttpServletRequest httpServletRequest,
                                              @RequestBody Map<String, Object> data){
        return adminService.updateExamineResult(httpServletRequest, Long.valueOf((String) data.get("examineId")), Integer.parseInt((String) data.get("result")));
    }


    /**
     * 获取管理界面首页信息
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/getIndexInfo")
    public Result<Map<String, Object>> reqIndexInfo(HttpServletRequest httpServletRequest){
        return adminService.reqIndexInfo(httpServletRequest);
    }


    /**
     * 获取管理员列表
     * @param httpServletRequest
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/getAdminAll")
    public Result<Map<String, Object>> getAdminAll(HttpServletRequest httpServletRequest,
                                                   @RequestParam("page") int page,
                                                   @RequestParam("pageSize") int pageSize){
        return adminService.getAdminAll(httpServletRequest, page, pageSize);
    }


    /**
     * 保存管理员信息
     * @param httpServletRequest
     * @param admin
     * @return
     */
    @PostMapping("/saveAdmin")
    public Result<String> saveAdmin(HttpServletRequest httpServletRequest,
                                     @RequestBody Admin admin){
        return adminService.saveAdmin(httpServletRequest, admin);
    }


    /**
     * 管理员搜索
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PostMapping("/searchAdmin")
    public Result<Map<String, Object>> searchAdmin(HttpServletRequest httpServletRequest,
                                                   @RequestBody Map<String, String> data){
        return adminService.searchAdmin(httpServletRequest, String.valueOf(data.get("content")));
    }


    /**
     * 重置管理员密码
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PutMapping("/resetAdmin")
    public Result<String> resetAdmin(HttpServletRequest httpServletRequest,
                                      @RequestBody List<Long> data){
        return adminService.resetAdmin(httpServletRequest, data);
    }

    /**
     * 激活管理员
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PutMapping("/returnAdmin")
    public Result<String> returnAdmin(HttpServletRequest httpServletRequest,
                                     @RequestBody List<Long> data){
        return adminService.returnAdmin(httpServletRequest, data);
    }

    /**
     * 删除管理员
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PutMapping("/removeAdmin")
    public Result<String> removeAdmin(HttpServletRequest httpServletRequest,
                                      @RequestBody List<Long> data){
        return adminService.removeAdmin(httpServletRequest, data);
    }


    /**
     * 修改管理员资料
     * @param httpServletRequest
     * @param admin
     * @return
     */
    @PutMapping("/updateAdminInfo")
    public Result<String> updateAdminInfo(HttpServletRequest httpServletRequest,
                                        @RequestBody Admin admin){
        return adminService.updateAdminInfo(httpServletRequest, admin);
    }

}
