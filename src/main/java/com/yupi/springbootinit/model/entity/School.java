package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学校信息实体类
 */
@Data
@TableName(value = "school")
public class School implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 学校唯一ID（主键）
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long schoolId;

    /**
     * 学校全称（含校区）
     */
    private String schoolFullName;

    /**
     * 学校主体首字母（大写）
     */
    private String schoolInitial;
    /**
     * 简介
     */
    private String introduction;
    /**
     * 学校图片
     */
    private String schoolImg;
    /**
     * 关联地区ID（外键）
     */
    private Long areaId;
    /**
     * 关联地区首字母
     */
    private String areaInital;
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
    