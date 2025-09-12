package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.model.dto.school.CityDetailDto;
import com.yupi.springbootinit.model.entity.CityDetail;
import com.yupi.springbootinit.model.vo.CityDetailVo;

public interface CityDetailService extends IService<CityDetail> {
    CityDetailVo getCityDetail(String cityName);
    Boolean saveOrUpdate(CityDetailDto cityDetailDto) throws Exception;
}
