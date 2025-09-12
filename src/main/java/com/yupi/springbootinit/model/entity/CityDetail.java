package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value ="city_detail")
public class CityDetail implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long cityDetailId;
    /**
     * 城市id
     */
    private Long cityId;
    /**
     * 城市名字
     */
    private String cityName;
    /**
     * 城市介绍
     */
    private String cityIntroduce;
    /**
     * 地理气候图1
     */
    private String geographicalClimateImg1;
    /**
     * 地理气候图2
     */
    private String geographicalClimateImg2;
    /**
     * 飞机
     */
    private String transportAviation;
    /**
     * 公交
     */
    private String transportRailway;
    /**
     * 地铁
     */
    private String transportSubway;
    /**
     * 最佳旅行季节
     */
    private String travelSeason;
    /**
     * 公交卡办理
     */
    private String card;
    /**
     * 住宿推荐
     */
    private String room;
    /**
     * 消费指南
     */
    private String consumerGuide;
    /**
     * 实用应用程序
     */
    private String practicalApp;
    /**
     * 注意事项
     */
    private String precautions;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
