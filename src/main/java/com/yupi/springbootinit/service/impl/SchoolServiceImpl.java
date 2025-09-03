package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.mapper.SchoolMapper;
import com.yupi.springbootinit.model.dto.school.SchoolDto;
import com.yupi.springbootinit.model.dto.school.SchoolSearchDto;
import com.yupi.springbootinit.model.entity.School;
import com.yupi.springbootinit.model.vo.SchoolVo;
import com.yupi.springbootinit.service.SchoolService;
import com.yupi.springbootinit.utils.ImageUploadUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements SchoolService {
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveSchool(SchoolDto schoolDto, MultipartFile imageFile) {
        String areaName = schoolDto.getAreaName();
        char c = areaName.charAt(0);
        String firstLetter = String.valueOf(Character.toUpperCase(c));
        if (!firstLetter.matches("[A-Z]")) {
            throw new RuntimeException("地区应为字母");
        }
        long areaId = (long) redisTemplate.opsForValue().get(areaName);
        if (areaName == null || areaName.isEmpty() || areaId <= 0) {
            throw new RuntimeException("地区不存在,请先添加地区");
        }
        School school = new School();
        String schoolFullName = schoolDto.getSchoolFullName();
        School one = this.getOne(Wrappers.lambdaQuery(School.class).eq(School::getSchoolFullName, schoolFullName));
        if (one == null) {
            String imgString = ImageUploadUtil.saveSingleImage(imageFile);
            school.setSchoolImg(imgString);
            char firstChar = schoolFullName.charAt(0);
            String first = String.valueOf(Character.toUpperCase(firstChar));
            BeanUtils.copyProperties(schoolDto, school);
            school.setAreaInital(firstLetter);
            school.setSchoolInitial(first);
            school.setAreaId(areaId);
            school.setCreateTime(new Date());
            return this.save(school);
        } else {
            throw new RuntimeException("学校已存在");
        }
    }

    @Override
    public List<SchoolVo> getSearchList(SchoolSearchDto schoolSearchDto) {
        LambdaQueryWrapper<School> wrapper = new LambdaQueryWrapper<>();
        // 处理Programs搜索条件
        handleProgramsCondition(schoolSearchDto, wrapper);
        // 处理大学名称首字母搜索条件
        handleUniversityNameCondition(schoolSearchDto, wrapper);
        // 处理城市首字母搜索条件
        handleCitiesCondition(schoolSearchDto, wrapper);
        // 处理热门城市搜索条件
        handlePopularCitiesCondition(schoolSearchDto, wrapper);
        // 直接流式处理转换，避免中间集合存储
        return this.list(wrapper).stream()
                .map(this::convertToSchoolVo)
                .collect(Collectors.toList());
    }

    // 拆分处理方法，避免单个方法内创建过多临时对象
    private void handleProgramsCondition(SchoolSearchDto dto, LambdaQueryWrapper<School> wrapper) {
        List<String> programs = dto.getSearchPrograms();
        if (programs == null || programs.isEmpty()) {
            return;
        }

        wrapper.and(w -> {
            for (String program : programs) {
                switch (program) {
                    case "Non Degree":
                        w.or().eq(School::getIsNonDegree, 1);
                        break;
                    case "Bachelor Degree":
                        w.or().eq(School::getIsBachelorDegree, 1);
                        break;
                    case "Master Degree":
                        w.or().eq(School::getIsMasterDegree, 1);
                        break;
                    case "PhD":
                        w.or().eq(School::getIsPhd, 1);
                        break;
                }
            }
        });
    }

    private void handleUniversityNameCondition(SchoolSearchDto dto, LambdaQueryWrapper<School> wrapper) {
        List<String> names = dto.getSearchUniversityName();
        if (names == null || names.isEmpty()) {
            return;
        }

        wrapper.and(w -> {
            for (String name : names) {
                if (name != null && name.matches("[A-Z]")) {
                    w.or().eq(School::getSchoolInitial, name);
                }
            }
        });
    }

    private void handleCitiesCondition(SchoolSearchDto dto, LambdaQueryWrapper<School> wrapper) {
        List<String> cities = dto.getSearchCities();
        if (cities == null || cities.isEmpty()) {
            return;
        }

        wrapper.and(w -> {
            for (String city : cities) {
                if (city != null && city.matches("[A-Z]")) {
                    w.or().eq(School::getAreaInital, city);
                }
            }
        });
    }

    private void handlePopularCitiesCondition(SchoolSearchDto dto, LambdaQueryWrapper<School> wrapper) {
        List<String> popularCities = dto.getSearchPopularCities();
        if (popularCities == null || popularCities.isEmpty()) {
            return;
        }

        wrapper.and(w -> {
            for (String city : popularCities) {
                if (city == null) {
                    continue;
                }
                // 提取首字母并大写
                char firstChar = city.charAt(0);
                if (!Character.isUpperCase(firstChar)) {
                    continue;
                }

                String lowerCity = city.toLowerCase();
                Object areaIdObj = redisTemplate.opsForValue().get(lowerCity);
                if (areaIdObj instanceof Long) {
                    w.or().eq(School::getAreaId, areaIdObj);
                } else {
                    // 使用常量代替魔法值
                    w.or().eq(School::getAreaId, -1L); // 无效ID，确保不会匹配
                }
            }
        });
    }

    // 单独的转换方法，避免在流中创建匿名对象
    private SchoolVo convertToSchoolVo(School school) {
        SchoolVo vo = new SchoolVo();
        BeanUtils.copyProperties(school, vo);
        return vo;
    }
}
