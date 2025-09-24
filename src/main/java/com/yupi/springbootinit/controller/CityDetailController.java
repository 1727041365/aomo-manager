package com.yupi.springbootinit.controller;

import com.yupi.springbootinit.annotation.IpWhitelist;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.model.dto.school.CityDetailDto;
import com.yupi.springbootinit.model.vo.CityDetailVo;
import com.yupi.springbootinit.service.CityDetailService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/cityDetail")
public class CityDetailController {
    @Resource
    private CityDetailService cityDetailService;

    @GetMapping("/get")
    public BaseResponse<CityDetailVo> getCityDetail(@RequestParam String cityName) {
        CityDetailVo cityDetailVo = cityDetailService.getCityDetail(cityName);
        return ResultUtils.success(cityDetailVo);
    }

    @RequestMapping("/create")
    @IpWhitelist({"144.34.224.28"})
    public BaseResponse<String> saveCityDetail(@RequestBody CityDetailDto cityDetailDto) throws Exception {
        Boolean result = cityDetailService.saveOrUpdate(cityDetailDto);
        if (result) {
            return ResultUtils.success("保存成功");
        } else {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "保存失败");
        }
    }

}
