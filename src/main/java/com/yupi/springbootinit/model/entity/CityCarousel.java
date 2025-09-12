package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 城市轮播图
 */
@Data
@TableName(value ="city_carousel")
public class CityCarousel implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_ID)
    private Long cityCarouselId;
    /**
     * 城市名字
     */
    private String cityName;
    /**
     * 轮播图分类
     */
    private String cityCarouselCategory;
    /**
     * 轮播图列表 - 需要使用 JSON 处理
     */
    @TableField(typeHandler = com.yupi.springbootinit.config.CarouselItemsTypeHandler.class)
    private List<CarouselItem> carouselItems;


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


    @Data
    public static class CarouselItem implements Serializable {
        /**
         * 轮播图图片
         */
        @JsonProperty("cityCarouselImg")
        private String cityCarouselImg;

        /**
         * 轮播图标题
         */
        @JsonProperty("title")
        private String title;

        /**
         * 轮播图描述1
         */
        @JsonProperty("description1")
        private String description1;

        /**
         * 轮播图描述2
         */
        @JsonProperty("description2")
        private String description2;

        /**
         * 轮播图描述3
         */
        @JsonProperty("description3")
        private String description3;

        /**
         * 轮播图描述4
         */
        @JsonProperty("description4")
        private String description4;
    }

}
