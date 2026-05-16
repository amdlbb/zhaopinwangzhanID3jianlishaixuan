package com.abc.xyzp.controller;

import com.abc.xyzp.entity.Result;
import com.abc.xyzp.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    /**
     * 上传图片
     * @param images
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("images") MultipartFile images,
                                 @RequestParam("flag") int flag,
                                 HttpServletRequest httpServletRequest){
        return imageService.upload(images, flag, httpServletRequest);
    }


    /**
     * 接收图片
     * @param name
     * @param httpServletResponse
     */
    @GetMapping("/download/{name}")
    public void download(@PathVariable("name") String name,
                         HttpServletResponse httpServletResponse){
        imageService.download(name, httpServletResponse);
    }


}
