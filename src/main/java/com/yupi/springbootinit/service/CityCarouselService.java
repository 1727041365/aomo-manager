package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.model.dto.school.CityCarouselDto;
import com.yupi.springbootinit.model.entity.CityCarousel;
import com.yupi.springbootinit.model.vo.CityCarouselVo;

import java.util.List;

public interface CityCarouselService extends IService<CityCarousel>{
    List<CityCarouselVo> getCityCarousel(String cityName);
    Boolean saveOrUpdate(CityCarouselDto CityCarouselDto);
}
