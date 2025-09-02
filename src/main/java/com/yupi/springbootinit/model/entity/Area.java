package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 地区信息实体类
 */
@Data
@TableName(value ="area")
public class Area implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 地区唯一ID（主键）
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long areaId;

    /**
     * 地区名称（城市级）
     */
    private String areaName;

    /**
     * 地区拼音首字母（大写）
     */
    private String areaInitial;

    /**
     * 记录创建时间
     */
    private Date createTime;

    /**
     * 记录更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除标识（0：未删除，1：已删除）
     */
    private Integer isDelete;
}
