package com.yupi.springbootinit.model.vo;


import lombok.Data;

import java.io.Serializable;
@Data
public class SchooMenuVo implements Serializable {
    private static final long serialVersionUID = 1L;

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
