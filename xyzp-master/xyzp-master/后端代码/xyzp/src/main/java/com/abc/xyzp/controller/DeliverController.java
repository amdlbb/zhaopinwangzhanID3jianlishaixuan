package com.abc.xyzp.controller;

import com.abc.xyzp.VO.DeliverVO;
import com.abc.xyzp.entity.Result;
import com.abc.xyzp.service.DeliverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/deliver")
public class DeliverController {

    @Autowired
    private DeliverService deliverService;

    @GetMapping("/list")
    public Result getDeliverList() {
        List<DeliverVO> deliverList = deliverService.getAllDeliverList();
        return Result.success(deliverList);
    }
}