package com.abc.xyzp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.abc.xyzp.common.MyConstants;
import com.abc.xyzp.common.exceptor.MyException;
import com.abc.xyzp.config.JwtConfig;
import com.abc.xyzp.dto.TeamDto;
import com.abc.xyzp.dto.UserDto;
import com.abc.xyzp.entity.Result;
import com.abc.xyzp.entity.Team;
import com.abc.xyzp.entity.User;
import com.abc.xyzp.mapper.TeamMapper;
import com.abc.xyzp.mapper.UserMapper;
import com.abc.xyzp.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ImageServiceImpl implements ImageService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private TeamMapper teamMapper;

    @Resource
    private JwtConfig jwtConfig;

    @Value("${Image.Path}")
    String ImagePath;

    @Value("${Image.URL}")
    String URL;

    @Override
    public Result<String>
    upload(MultipartFile images, int flag, HttpServletRequest httpServletRequest) {

        // 获取原文件名的后缀
        String s = images.getOriginalFilename().substring(images.getOriginalFilename().indexOf("."));

        // 使用UUID重新生成文件名，防止文件名重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + s;

        try {
            // 若文件目录不存在就创建一个
            File nFile = new File(ImagePath);
            if(!nFile.exists()){
                nFile.mkdirs();
            }
            // 转存文件
            images.transferTo(new File(ImagePath + fileName));
        } catch (IOException e) {
            throw new MyException("保存图片出错");
        }

        String url = URL + "api/image/download/" + fileName;

        // 判断上传的照片类型
        if(flag == 0){
            String userId = String.valueOf(httpServletRequest.getAttribute("userId"));
            // 判断是用户还是团队
            int myChoice = (int) redisTemplate.opsForValue().get(MyConstants.MYCHOICE + userId);
            if(myChoice == 0){
                // 修改用户头像
                User user = JSONUtil.toBean((String) redisTemplate.opsForValue().get(MyConstants.USERINFO + userId), User.class);
                user.setAvatar(url);
                if(userMapper.updateById(user) <= 0){
                    throw new MyException("更新头像出错");
                }
                // 修改redis中的数据
                redisTemplate.opsForValue().set(MyConstants.USERINFO + userId, JSONUtil.toJsonStr(user), 1, TimeUnit.HOURS);
            }
            else {
                Team team = JSONUtil.toBean((String) redisTemplate.opsForValue().get(MyConstants.TEAMINFO + userId), Team.class);
                team.setAvatar(url);
                if(teamMapper.updateById(team) <= 0){
                    throw new MyException("更新头像出错");
                }
                // 修改redis中的数据
                redisTemplate.opsForValue().set(MyConstants.TEAMINFO + userId, JSONUtil.toJsonStr(team), 1, TimeUnit.HOURS);
            }


        }
        return Result.success(url);
    }


    @Override
    public void download(String name, HttpServletResponse httpServletResponse){

        try{
            // 输入流
            FileInputStream fileInputStream = new FileInputStream(new File(ImagePath + name));

            // 输出流，将文件写回浏览器
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            httpServletResponse.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            // 读取文件，数据存入bytes数组中
            while((len = fileInputStream.read(bytes)) != -1){
                // 从bytes中写出文件到输出流中
                servletOutputStream.write(bytes, 0, len);
                // 刷新
                servletOutputStream.flush();
            }

            // 关闭资源
            servletOutputStream.close();
            fileInputStream.close();
        } catch (Exception e){
            throw new MyException("加载图片出错");
        }

    }
}
