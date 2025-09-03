package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.model.dto.school.SchoolDto;
import com.yupi.springbootinit.model.dto.school.SchoolSearchDto;
import com.yupi.springbootinit.model.entity.School;
import com.yupi.springbootinit.model.vo.SchoolVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SchoolService extends IService<School> {
    Boolean saveSchool(SchoolDto schoolDto, MultipartFile imageFile);

    List<SchoolVo> getSearchList(SchoolSearchDto schoolSearchDto);
}
