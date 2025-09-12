package com.yupi.springbootinit.controller;

import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.utils.ImageUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/upload")
public class UploadController {

    @PostMapping("/image")
    public BaseResponse<String> uploadImage(MultipartFile file) {
        return ResultUtils.success(ImageUploadUtil.saveSingleImage(file));
    }
    @PostMapping("/video")
    public BaseResponse<String> video(MultipartFile file) throws IOException {
        return ResultUtils.success(ImageUploadUtil.saveVideoFile(file));
    }
}
