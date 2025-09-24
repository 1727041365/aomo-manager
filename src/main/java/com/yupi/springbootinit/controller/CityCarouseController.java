package com.yupi.springbootinit.controller;

import com.yupi.springbootinit.annotation.IpWhitelist;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.model.dto.school.CityCarouselDto;
import com.yupi.springbootinit.model.vo.CityCarouselVo;
import com.yupi.springbootinit.service.CityCarouselService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/cityCarouse")
public class CityCarouseController {
    @Resource
    private CityCarouselService cityCarouselService;
    @RequestMapping("/get")
    public BaseResponse<List<CityCarouselVo>> getCityCarousel(@RequestParam String cityName){
        List<CityCarouselVo> cityCarouselVoList = cityCarouselService.getCityCarousel(cityName);
        return ResultUtils.success(cityCarouselVoList);
    }
    @RequestMapping("/save")
    @IpWhitelist({"144.34.224.28"})
    public BaseResponse<String> saveCityCarousel(@RequestBody CityCarouselDto cityCarouselDto){
        boolean save = cityCarouselService.saveOrUpdate(cityCarouselDto);
        if (!save){
            return ResultUtils.error(ErrorCode.OPERATION_ERROR,"保存失败");
        }
        return ResultUtils.success("保存成功");
    }
}
