package com.yupi.springbootinit.controller;

import com.yupi.springbootinit.service.SchoolDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/ProgramsDetail")
public class SchoolDetailController {
    @Resource
    private SchoolDetailService schoolDetailService;
    @GetMapping("/getIntroduction")
    public String getSchoolIntroduction() {
        return "hello world";
    }
    @GetMapping("/getNonDegree")
    public String getNonDegree() {
        return "hello world";
    }
    @GetMapping("/getBachelorDegree")
    public String getBachelorDegree() {
        return "hello world";
    }
    @GetMapping("/getMasterDegree")
    public String getMasterDegree() {
        return "hello world";
    }
    @GetMapping("/getPhd")
    public String getPhd() {
        return "hello world";
    }
}
