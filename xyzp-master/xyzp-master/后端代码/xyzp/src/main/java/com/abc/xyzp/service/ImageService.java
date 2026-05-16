package com.abc.xyzp.service;

import com.abc.xyzp.entity.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ImageService {

    Result<String> upload(MultipartFile images, int flag, HttpServletRequest httpServletRequest);

    void download(String name, HttpServletResponse httpServletResponse);
}
