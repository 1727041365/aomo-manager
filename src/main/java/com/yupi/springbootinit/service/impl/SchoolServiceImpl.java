package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.mapper.SchoolMapper;
import com.yupi.springbootinit.model.dto.SchoolDto;
import com.yupi.springbootinit.model.entity.School;
import com.yupi.springbootinit.service.SchoolService;
import com.yupi.springbootinit.utils.ImageUploadUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements SchoolService {
    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public Boolean saveSchool(SchoolDto schoolDto, MultipartFile imageFile) {
        String areaName = schoolDto.getAreaName();
        long areaId = (long) redisTemplate.opsForValue().get(areaName);
        if  (areaName == null || areaName.isEmpty() || areaId <= 0){
            throw new RuntimeException("地区不存在,请先添加地区");
        }
        School school = new School();
        String schoolFullName = schoolDto.getSchoolFullName();
        School one = this.getOne(Wrappers.lambdaQuery(School.class).eq(School::getSchoolFullName, schoolFullName));
        if (one==null){
            String imgString = ImageUploadUtil.saveSingleImage(imageFile);
            school.setSchoolImg(imgString);
            char firstChar = schoolFullName.charAt(0);
            String first = String.valueOf(Character.toUpperCase(firstChar));
            BeanUtils.copyProperties(schoolDto,school);
            school.setSchoolInitial(first);
            school.setAreaId(areaId);
            school.setCreateTime(new Date());
            return this.save(school);
        }else {
            throw new RuntimeException("学校已存在");
        }
    }
}
