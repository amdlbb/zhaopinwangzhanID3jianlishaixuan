package com.abc.xyzp.controller;

import com.abc.xyzp.dto.TeamDto;
import com.abc.xyzp.dto.UserResumeDto;
import com.abc.xyzp.entity.*;
import com.abc.xyzp.service.TeamService;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    @Resource
    private TeamService teamService;

    /**
     * 查找团队信息
     * @param httpServletRequest
     * @return
     */
    @GetMapping
    public Result<TeamDto> getTeamInfo(HttpServletRequest httpServletRequest){
        return teamService.getTeamInfo(httpServletRequest);
    }


    /**
     * 查找团队信息带id
     * @param httpServletRequest
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<TeamDto> getTeamInfo2(HttpServletRequest httpServletRequest,
                                       @PathVariable("id") Long id){
        return teamService.getTeamInfo2(httpServletRequest, id);
    }

    /**
     * 更新团队信息
     * @param httpServletRequest
     * @param team
     * @return
     */
    @PutMapping("/updateTeamInfo")
    public Result<String> updateTeamInfo(HttpServletRequest httpServletRequest,
                                         @RequestBody Team team){
        return teamService.updateTeamInfo(team, httpServletRequest);
    }


    /**
     * 更新团队密码
     * @param httpServletRequest
     * @param passwordForm
     * @return
     */
    @PutMapping("/updateTeamPassword")
    public Result<String> updateTeamPassword(HttpServletRequest httpServletRequest,
                                         @RequestBody PasswordForm passwordForm){
        return teamService.updateTeamPassword(passwordForm, httpServletRequest);
    }


    /**
     * 发布团队招聘信息
     * @param httpServletRequest
     * @param teamJob
     * @return
     */
    @PostMapping("/saveTeamJob")
    public Result<String> saveTeamJob(HttpServletRequest httpServletRequest,
                                      @RequestBody TeamJob teamJob){
        return teamService.saveTeamJob(httpServletRequest, teamJob);
    }


    /**
     * 获取团队的所有招聘信息
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/getTeamJob")
    public Result<Map<String, Object>> getTeamJob(HttpServletRequest httpServletRequest,
                                            @RequestParam("page") int page,
                                            @RequestParam("pageSize") int pageSize){
        return teamService.getTeamJob(httpServletRequest, page, pageSize);
    }


    /**
     * 搜索团队招聘信息
     * @param httpServletRequest
     * @param content
     * @return
     */
    @GetMapping("/searchTeamJob")
    public Result<Map<String, Object>> searchTeamJob(HttpServletRequest httpServletRequest,
                                                     @RequestParam("content") String content){
        return teamService.searchTeamJob(httpServletRequest, content);
    }


    /**
     * 获取最新的团队招聘信息
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/getNewTeamJob")
    public Result<Map<String, Object>> getNewTeamJob(@RequestParam("page") int page,
                                                     @RequestParam("pageSize") int pageSize){
        return teamService.getNewTeamJob(page, pageSize);
    }


    /**
     * 获取某个团队招聘信息
     * @param id
     * @return
     */
    @GetMapping("/getTeamJobInfo/{id}")
    public Result<TeamJob> getTeamJobInfo(@PathVariable("id") Long id){
        return teamService.getTeamJobInfo(id);
    }


    /**
     * 更新团队职位信息
     * @param httpServletRequest
     * @param teamJob
     * @return
     */
    @PutMapping("/updateTeamJob")
    public Result<String> updateTeamJob(HttpServletRequest httpServletRequest,
                                        @RequestBody TeamJob teamJob){
        return teamService.updateTeamJob(httpServletRequest, teamJob);
    }


    /**
     * 获取团队收到的简历
     * @param httpServletRequest
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/getUserResume")
    public Result<Map<String, Object>> getUserResume1(HttpServletRequest httpServletRequest,
                                                      @RequestParam("page") int page,
                                                      @RequestParam("pageSize") int pageSize){
        return teamService.getUserResume(httpServletRequest, page, pageSize);
    }


    /**
     * 搜索团队收到的用户简历
     * @param httpServletRequest
     * @param content
     * @return
     */
    @GetMapping("/searchUserResume")
    public Result<Map<String, Object>> searchUserResume(HttpServletRequest httpServletRequest,
                                                      @RequestParam("content") String content){
        return teamService.searchUserResume(httpServletRequest, content);
    }

    /**
     * 更新团队招聘信状态
     * @param httpServletRequest
     * @param data
     * @return
     */
   @PutMapping("/updateTeamJobStatus")
   public Result<String> updateTeamJobStatus(HttpServletRequest httpServletRequest,
                                              @RequestBody Map<String, Long> data){
       return teamService.updateTeamJobStatus(httpServletRequest, data.get("id"), data.get("status"));
   }


    /**
     * 设置为面试者
     * @param httpServletRequest
     * @param data
     * @return
     */
   @PostMapping("/saveInterView")
   public Result<String> saveInterView(HttpServletRequest httpServletRequest,
                                       @RequestBody Map<String, Object> data){
       return teamService.saveInterView(httpServletRequest, data);
   }


    /**
     * 更新投递状态
     * @param httpServletRequest
     * @param data
     * @return
     */
   @PostMapping("/updateDeliverStatus")
   public Result<String> updateDeliverStatus(HttpServletRequest httpServletRequest,
                                       @RequestBody Map<String, Object> data){
       return teamService.updateDeliverStatus(httpServletRequest, data);
   }


    /**
     * 获取团队面试人员
     * @param httpServletRequest
     * @param page
     * @param pageSize
     * @return
     */
   @GetMapping("/getInterviewInfo")
   public Result<Map<String, Object>> getInterviewInfo(HttpServletRequest httpServletRequest,
                                                     @RequestParam("page") int page,
                                                     @RequestParam("pageSize") int pageSize){
       return teamService.getInterviewInfo(httpServletRequest, page, pageSize);
   }


    /**
     * 团队招聘信息录取
     * @param httpServletRequest
     * @param data
     * @return
     */
   @PostMapping("/admissionUser")
   public Result<String> admissionUser(HttpServletRequest httpServletRequest,
                                       @RequestBody Map<String, Object> data){
       return teamService.admissionUser(httpServletRequest, data);
   }


    /**
     * 更新面试状态
     * @param httpServletRequest
     * @param data
     * @return
     */
   @PutMapping("/updateInterviewStatus")
   public Result<String> updateInterviewStatus(HttpServletRequest httpServletRequest,
                                       @RequestBody Map<String, Object> data){
       return teamService.updateInterviewStatus(httpServletRequest, data);
   }


    /**
     * 搜索面试者
     * @param httpServletRequest
     * @param content
     * @return
     */
    @GetMapping("/searchInterview")
    public Result<Map<String, Object>> searchInterview(HttpServletRequest httpServletRequest,
                                                        @RequestParam("content") String content){
        return teamService.searchInterview(httpServletRequest, content);
    }


    /**
     * 获取录取信息
     * @param httpServletRequest
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/getAdmissionInfo")
    public Result<Map<String, Object>> getAdmissionInfo(HttpServletRequest httpServletRequest,
                                                        @RequestParam("page") int page,
                                                        @RequestParam("pageSize") int pageSize){
        return teamService.getAdmissionInfo(httpServletRequest, page, pageSize);
    }

    /**
     * 获取所有团队的录取信息（不限团队）
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/getAllAdmissionInfo")
    public Result<Map<String, Object>> getAllAdmissionInfo(HttpServletRequest httpServletRequest,
                                                           @RequestParam("page") int page,
                                                           @RequestParam("pageSize") int pageSize){
        return teamService.getAllAdmissionInfo(page, pageSize);
    }


    /**
     * 搜索团队录取信息
     * @param httpServletRequest
     * @param content
     * @return
     */
    @GetMapping("/searchAdmission")
    public Result<Map<String, Object>> searchAdmission(HttpServletRequest httpServletRequest,
                                                        @RequestParam("content") String content){
        return teamService.searchAdmission(httpServletRequest, content);
    }


    /**
     * 随机推荐团队招聘信息
     * @return
     */
    @GetMapping("/recommendTeamJob")
    public Result<List<TeamJob>> recommendTeamJob(){
        return teamService.recommendTeamJob();
    }


    /**
     * 搜索团队招聘
     * @param content
     * @return
     */
    @GetMapping("/searchIndexTeamJob")
    public Result< List<TeamJob>> searchIndexTeamJob(@RequestParam("content") String content){
        return teamService.searchIndexTeamJob(content);
    }


    /**
     * 获取团队列表（仅限管理员）
     * @param httpServletRequest
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/getTeamAll")
    public Result<Map<String, Object>> getTeamAll(HttpServletRequest httpServletRequest,
                                                  @RequestParam("page") int page,
                                                  @RequestParam("pageSize") int pageSize){
        return teamService.getTeamAll(httpServletRequest, page, pageSize);
    }


    /**
     * 获取团队招聘审核表
     * @param httpServletRequest
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/getTeamJobExamine")
    public Result<Map<String, Object>> getTeamJobExamine(HttpServletRequest httpServletRequest,
                                                  @RequestParam("page") int page,
                                                  @RequestParam("pageSize") int pageSize){
        return teamService.getTeamJobExamine(httpServletRequest, page, pageSize);
    }


    /**
     * 审核不合格的团队招聘重新提交审核
     * @param data
     * @return
     */
    @PutMapping("/updateAgainChangeTeamJobStatus")
    public Result<String> updateAgainChangeTeamJobStatus(HttpServletRequest httpServletRequest,
                                                      @RequestBody Map<String, Object> data){
        return teamService.updateAgainChangeTeamJobStatus(httpServletRequest, Long.valueOf((String) data.get("id")), Long.valueOf(String.valueOf(data.get("status"))));
    }

    /**
     * 管理员搜索团队信息
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PostMapping("/adminSearchTeam")
    public Result<Map<String, Object>> adminSearchTeam(HttpServletRequest httpServletRequest,
                                                       @RequestBody Map<String, String> data){
        return teamService.adminSearchTeam(httpServletRequest, String.valueOf(data.get("content")));
    }


    /**
     * 管理员删除团队
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PostMapping("/adminRemoveTeam")
    public Result<String> adminRemoveTeam(HttpServletRequest httpServletRequest,
                                       @RequestBody List<Long> data){
        return teamService.adminRemoveTeam(httpServletRequest, data);
    }


    /**
     * 管理员恢复团队
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PostMapping("/adminReturnTeam")
    public Result<String> adminReturnTeam(HttpServletRequest httpServletRequest,
                                          @RequestBody List<Long> data){
        return teamService.adminReturnTeam(httpServletRequest, data);
    }


    /**
     * 重置团队密码
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PutMapping("/adminResetTeam")
    public Result<String> adminResetTeam(HttpServletRequest httpServletRequest,
                                         @RequestBody List<Long> data){
        return teamService.adminResetTeam(httpServletRequest, data);
    }

    /**
     * 管理员获取全部的审核
     * @param httpServletRequest
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/adminTeamJobExamineAll")
    public Result<Map<String, Object>> adminTeamJobExamineAll(HttpServletRequest httpServletRequest,
                                                         @RequestParam("page") int page,
                                                         @RequestParam("pageSize") int pageSize){
        return teamService.adminTeamJobExamineAll(httpServletRequest, page, pageSize);
    }


    /**
     * 管理员更改审核表状态
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PutMapping("/adminChangeExamineStatus")
    public Result<String> adminChangeExamineStatus(HttpServletRequest httpServletRequest,
                                                              @RequestBody Map<String, String> data){
        return teamService.adminChangeExamineStatus(httpServletRequest, Long.parseLong(data.get("id")),
                Integer.parseInt(data.get("result")), Long.parseLong(data.get("teamId")), Long.parseLong(data.get("teamJobId")));
    }


    /**
     * 管理员搜索审核表
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PostMapping("/adminSearchExamine")
    public Result<Map<String, Object>> adminSearchExamine(HttpServletRequest httpServletRequest,
                                                   @RequestBody Map<String, String> data){
        return teamService.adminSearchExamine(httpServletRequest, data.get("content"));
    }

}


