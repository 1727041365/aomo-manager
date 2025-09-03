package com.yupi.springbootinit.model.dto.school;

import lombok.Data;

import java.io.Serializable;

@Data
public class SchoolDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 学校全称（含校区）
     */
    private String schoolFullName;

    /**
     * 地区
     */
    private String areaName;
    /**
     * 简介
     */
    private String introduction;
    /**
     * 是否有汉语言（0：无，1：有）
     */
    private Integer isNonDegree;

    /**
     * 是否有博士学位（0：无，1：有）
     */
    private Integer isPhd;
    /**
     * 是否有学士学位（0：无，1：有）
     */
    private Integer isBachelorDegree;

    /**
     * 是否有硕士学位（0：无，1：有）
     */
    private Integer isMasterDegree;
}
