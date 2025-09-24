package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.mapper.StudyTourMapper;
import com.yupi.springbootinit.model.entity.StudyTour;
import com.yupi.springbootinit.service.StudyTourService;
import org.springframework.stereotype.Service;

@Service
public class StudyTourServiceImpl extends ServiceImpl<StudyTourMapper, StudyTour> implements StudyTourService {
}
