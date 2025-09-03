package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.mapper.SchoolDetailMapper;
import com.yupi.springbootinit.model.entity.SchoolDetail;
import com.yupi.springbootinit.service.SchoolDetailService;
import org.springframework.stereotype.Service;

@Service
public class SchoolDetailServiceImpl extends ServiceImpl<SchoolDetailMapper, SchoolDetail> implements SchoolDetailService {
}
