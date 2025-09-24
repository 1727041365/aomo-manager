package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.annotation.IpWhitelist;
import com.yupi.springbootinit.annotation.RateLimiter;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.model.dto.StudyTourDto;
import com.yupi.springbootinit.model.entity.StudyTour;
import com.yupi.springbootinit.service.StudyTourService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/tour")
public class StudyTourController {
    @Autowired
    private StudyTourService studyTourService;
    @GetMapping("/emails")
    @IpWhitelist({"144.34.224.28"})
    public BaseResponse<Page<StudyTour>> getForm(@RequestParam(defaultValue = "1") int pageNumber,
                                                 @RequestParam(defaultValue = "10") int pageSize) {
        // 参数校验
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }
        // 创建分页对象，按创建时间倒序排列
        Page<StudyTour> page = new Page<>(pageNumber, pageSize);
        Page<StudyTour> resultPage = studyTourService.page(page,
                Wrappers.<StudyTour>lambdaQuery()
                        .orderByDesc(StudyTour::getApplyTime)); // 按创建时间由新到老排序
        return ResultUtils.success(resultPage);
    }
    @PostMapping("/save")
    @RateLimiter(limit = 5, timeout = 60) // 限制每个IP每分钟最多5次请求
    public BaseResponse<String> saveForm(@Valid @RequestBody StudyTourDto tourFormDate) {
        // 检查是否已存在相同邮箱或手机号的表单
        StudyTour existingForm = studyTourService.getOne(Wrappers.<StudyTour>lambdaQuery()
                .eq(StudyTour::getEmail, tourFormDate.getEmail())
                .or()
                .eq(StudyTour::getPhone, tourFormDate.getPhone()));
        // 如果已存在相同表单，则不重复保存
        if (existingForm != null) {
            return ResultUtils.success("We have received your request and will reply to you later!");
        }
        StudyTour studyTour = new StudyTour();
        BeanUtils.copyProperties(tourFormDate, studyTour);
        // 设置表单状态为待处理
        studyTour.setFormStatus("pending");
        boolean save = studyTourService.save(studyTour);
        if (save) {
            return ResultUtils.success("We have received your request and will reply to you later!");
        } else {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "Please try again");
        }
    }

}
