package com.yupi.springbootinit.model.dto.school;

import com.yupi.springbootinit.model.entity.CityCarousel;
import com.yupi.springbootinit.model.vo.CityCarouselVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CityCarouselDto implements Serializable {
    /**
     * 城市名字
     */
    private String cityName;
    /**
     * 轮播图分类
     */
    private String cityCarouselCategory;
    /**
     * 轮播图列表
     */
    private List<CityCarousel.CarouselItem> carouselItems;
}
