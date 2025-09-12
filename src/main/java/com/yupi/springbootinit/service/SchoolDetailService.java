package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.model.dto.school.SchoolInfoDTO;
import com.yupi.springbootinit.model.entity.SchoolDetail;

public interface SchoolDetailService extends IService<SchoolDetail>{
    Boolean saveDetail(SchoolInfoDTO schoolInfoDTO) throws Exception;
}
