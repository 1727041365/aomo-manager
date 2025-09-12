package com.yupi.springbootinit.model.dto.school;

import lombok.Data;

import java.io.Serializable;

@Data
public class SchoolInfoDTO implements Serializable {
    private Long schoolId;
    private String schoolLogo;
    private String schoolIntroduction;
    private String bachelorDegree;
    private String chineseLanguageProgram;
    private String masterDegree;
    private String phd;
    private String schoolScenery;
    private String sceneryVideo;
}