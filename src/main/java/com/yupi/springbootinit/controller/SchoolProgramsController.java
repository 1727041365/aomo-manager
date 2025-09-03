package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.model.dto.school.SchoolDto;
import com.yupi.springbootinit.model.dto.school.SchoolSearchDto;
import com.yupi.springbootinit.model.entity.School;
import com.yupi.springbootinit.model.vo.SchooMenuVo;
import com.yupi.springbootinit.model.vo.SchoolVo;
import com.yupi.springbootinit.service.AreaService;
import com.yupi.springbootinit.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/school")
public class SchoolProgramsController {
    @Autowired
    private SchoolService schoolService;
    @Resource
    private AreaService areaService;

    @GetMapping("get")
    public BaseResponse<List<SchoolVo>> get(@RequestParam("pageName") long pagename, @RequestParam("pageSize") long pageSize) {
        Page<School> schoolPage = new Page<>(pagename, pageSize);
        // 执行分页查询
        Page<School> resultPage = schoolService.page(schoolPage);
        List<SchoolVo> schoolVoList = resultPage.getRecords().stream().map(item -> {
            SchoolVo schoolVo = new SchoolVo();
            BeanUtils.copyProperties(item, schoolVo);
            return schoolVo;
        }).collect(Collectors.toList());
        return ResultUtils.success(schoolVoList);
    }

    @PostMapping("/search")
    public BaseResponse<List<SchoolVo>> search(@RequestBody SchoolSearchDto schoolSearchDto) {
        if (schoolSearchDto == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        List<SchoolVo> schoolVo = schoolService.getSearchList(schoolSearchDto);
        return ResultUtils.success(schoolVo);
    }
    @GetMapping("/getMenu")
    public BaseResponse<SchooMenuVo> getMenu(@RequestParam("schoolId") String id ) {
        if (id == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        long l = Long.parseLong(id);
        School school = schoolService.getById( l);
        SchooMenuVo schoolMenuVo = new SchooMenuVo();
        BeanUtils.copyProperties(school, schoolMenuVo);
        return ResultUtils.success(schoolMenuVo);
    }


    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<String> add(@ModelAttribute SchoolDto schoolDto
            , @RequestPart("file") MultipartFile imageFile) {
        if (schoolDto == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (schoolDto.getAreaName() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "地区名字为空");
        }
        ;
        if (schoolDto.getSchoolFullName() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "学校全称为空");
        }
        if (schoolDto.getIntroduction() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "学校简介为空");
        }
        Boolean result = schoolService.saveSchool(schoolDto, imageFile);
        if (result) {
            return ResultUtils.success("添加成功");
        } else {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "添加失败");
        }
    }


    @PostMapping("/delete")
    public BaseResponse<String> delete(@RequestParam("schoolFullName") String schoolFullName) {
        if (schoolFullName == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        Boolean result = schoolService.remove(Wrappers.lambdaQuery(School.class).eq(School::getSchoolFullName, schoolFullName));
        if (result) {
            return ResultUtils.success("删除成功");
        } else {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "删除失败");
        }
    }
}
