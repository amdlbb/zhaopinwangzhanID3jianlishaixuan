package com.abc.xyzp.controller;

import com.abc.xyzp.dto.AdmissionDto;
import com.abc.xyzp.dto.UserDto;
import com.abc.xyzp.entity.*;
import com.abc.xyzp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.LinkOption;
import java.util.List;
import java.util.Map;


@RequestMapping("/api/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询用户信息1
     * @param httpServletRequest
     * @return
     */
    @GetMapping
    public Result<UserDto> getUserInfo(HttpServletRequest httpServletRequest){
        return userService.getUserInfo(httpServletRequest);
    }


    /**
     * 查询用户信息2，用于查询带编号的用户
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public Result<UserDto> getUserInfo2(@PathVariable("userId") Long userId){
        return userService.getUserInfo2(userId);
    }

    /**
     * 获取用户简历
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/getUserResume")
    public Result<UserResume> getUserResume(HttpServletRequest httpServletRequest){
        return userService.getUserResume(httpServletRequest);
    }

    /**
     * 更新用户信息
     * @param user
     * @param httpServletRequest
     * @return
     */
    @PutMapping("/updateUserInfo")
    public Result<String> updateUserInfo(@RequestBody User user,
                                         HttpServletRequest httpServletRequest){
        return userService.updateUserInfo(user, httpServletRequest);
    }

    /**
     * 更新用户简历
     * @param userResume
     * @param httpServletRequest
     * @return
     */
    @PutMapping("/updateUserResume")
    public Result<String> updateUserResume(@RequestBody UserResume userResume,
                                           HttpServletRequest httpServletRequest){
        return userService.updateUserResume(userResume, httpServletRequest);
    }

    /**
     * 更新用户密码
     * @param passwordForm
     * @param httpServletRequest
     * @return
     */
    @PutMapping("/updateUserPassword")
    public Result<String> updateUserPassword(@RequestBody PasswordForm passwordForm,
                                             HttpServletRequest httpServletRequest){
        return userService.updateUserPassword(passwordForm, httpServletRequest);
    }


    /**
     * 获取用户投递的简历
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/getUserDeliver")
    public Result<Map<String, Object>> getUserDeliver(HttpServletRequest httpServletRequest,
                                                    @RequestParam("page") int page,
                                                    @RequestParam("pageSize") int pageSize){
        return userService.getUserDeliver(httpServletRequest, page, pageSize);
    }


    /**
     * 用户取消投递
     * @param httpServletRequest
     * @param deliverId
     * @return
     */
    @PutMapping("/cancelUserDeliver")
    public Result<String> cancelUserDeliver(HttpServletRequest httpServletRequest,
                                            @RequestBody Map<String, Long> deliverId){
        return userService.cancelUserDeliver(httpServletRequest, deliverId.get("id"));
    }


    /**
     * 搜索用户投递
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PostMapping("/getUserDeliver")
    public Result<List<UserDeliver>> searchUserDeliver(HttpServletRequest httpServletRequest,
                                                       @RequestBody Map<String, String> data){
        return userService.searchUserDeliver(httpServletRequest, data.get("content"));
    }


    /**
     * 查询所有用户信息（仅限管理员）
     * @param httpServletRequest
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("getUserAll")
    public Result<Map<String, Object>> getUserAll(HttpServletRequest httpServletRequest,
                                                  @RequestParam("page") int page,
                                                  @RequestParam("pageSize") int pageSize){
        return userService.getUserALl(httpServletRequest, page, pageSize);
    }


    /**
     * 管理员搜索用户信息
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PostMapping("/adminSearchUser")
    public Result<Map<String, Object>> adminSearchUser(HttpServletRequest httpServletRequest,
                                                       @RequestBody Map<String, String> data){
        return userService.adminSearchUser(httpServletRequest, data.get("content"));
    }


    /**
     * 管理员删除用户
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PutMapping("/adminRemoveUser")
    public Result<String> adminRemoveUser(HttpServletRequest httpServletRequest,
                                          @RequestBody List<Long> data){
        return userService.adminRemoveUser(httpServletRequest, data);
    }

    /**
     * 管理员恢复用户
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PutMapping("/adminReturnUser")
    public Result<String> adminReturnUser(HttpServletRequest httpServletRequest,
                                          @RequestBody List<Long> data){
        return userService.adminReturnUser(httpServletRequest, data);
    }

    /**
     * 重置用户密码
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PutMapping("/adminResetUser")
    public Result<String> adminResetUser(HttpServletRequest httpServletRequest,
                                         @RequestBody List<Long> data){
        return userService.adminResetUser(httpServletRequest, data);
    }


    /**
     * 保存用户投递
     * @param httpServletRequest
     * @param data
     * @return
     */
    @PostMapping("/saveUserDeliver")
    public Result<String> saveUserDeliver(HttpServletRequest httpServletRequest,
                                         @RequestBody Map<String, Long> data){
        return userService.saveUserDeliver(httpServletRequest, data.get("teamJobId"));
    }


    /**
     * 获取用户的所有录取
     * @param httpServletRequest
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/getAdmission")
    public Result<Map<String, Object>> getAdmission(HttpServletRequest httpServletRequest,
                                             @RequestParam("page") int page,
                                             @RequestParam("pageSize") int pageSize){
        return userService.getAdmission(httpServletRequest, page, pageSize);
    }


    /**
     * 搜索用户录取
     * @param httpServletRequest
     * @param data
     * @return
     */
    @GetMapping("/searchUserAdmisson")
    public Result<Map<String, Object>> searchUserAdmisson(HttpServletRequest httpServletRequest,
                                                    @RequestParam("content") String data){
        return userService.searchUserAdmisson(httpServletRequest, data);
    }

}

