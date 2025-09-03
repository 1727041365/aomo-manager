package com.yupi.springbootinit.model.dto.school;

import lombok.Data;

import java.io.Serializable;

@Data
public class AreaDto  implements Serializable {
    /**
     * 地区名称（城市级）
     */
    private String areaName;

}
