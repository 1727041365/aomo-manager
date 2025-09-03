package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学校数据详情类
 */
@Data
public class SchoolDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long schoolDetailId;
    /**
     * 学校id
     */
    private Long schoolId;
    /**
     * 学校logo
     */
    private String schoolLogo;
    /**
     * 学校介绍
     */
    private String schoolIntroduction;
    /**
     * 学校风景
     */
    private String  schoolScenery;
    /**
     * 硕士学位介绍
     */
    private String bachelorDegree;
    /**
     * 学士学位介绍
     */
    private String masterDegree;
    /**
     * 博士学位介绍
     */
    private String phd;
    /**
     * 汉语言介绍
     */
    private String chineseLanguageProgram;
    /**
     * 记录创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 记录更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 逻辑删除标识（0：未删除，1：已删除）
     */
    private Integer isDelete;
}
