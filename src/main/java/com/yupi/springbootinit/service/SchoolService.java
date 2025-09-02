package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.model.dto.SchoolDto;
import com.yupi.springbootinit.model.entity.School;
import org.springframework.web.multipart.MultipartFile;

public interface SchoolService extends IService<School> {
    Boolean saveSchool(SchoolDto schoolDto, MultipartFile imageFile);
}
