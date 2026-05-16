package com.abc.xyzp.controller;

import com.abc.xyzp.entity.Result;
import com.abc.xyzp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class
EmailController {

    @Autowired
    private EmailService emailService;

    /**
     * 注册邮件
     * @param uuid
     * @param email
     * @return
     */
    @GetMapping()
    public Result<String> send(@RequestParam("uuid") String uuid, @RequestParam("email") String email){
        return emailService.send(uuid, email);
    }

    /**
     * 通知面试邮件
     * @param httpServletRequest
     * @param data
     * @return
     */
    @GetMapping("sendInterview")
    public Result<String> sendInterview(HttpServletRequest httpServletRequest,
                               @RequestParam Map<String, Object> data){
        return emailService.sendInterview(httpServletRequest, data);
    }

    /**
     * 通知录取结果
     * @param httpServletRequest
     * @param data
     * @return
     */
    @GetMapping("sendAdmission")
    public Result<String> sendAdmission(HttpServletRequest httpServletRequest,
                                        @RequestParam Map<String, Object> data){
        return emailService.sendAdmission(httpServletRequest, data);
    }



}
