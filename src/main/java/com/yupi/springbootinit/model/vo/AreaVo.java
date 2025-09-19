package com.yupi.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class AreaVo implements Serializable {
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

}
