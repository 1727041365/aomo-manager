package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.mapper.CityDetailMapper;
import com.yupi.springbootinit.model.dto.school.CityDetailDto;
import com.yupi.springbootinit.model.entity.CityDetail;
import com.yupi.springbootinit.model.vo.CityDetailVo;
import com.yupi.springbootinit.service.CityDetailService;
import com.yupi.springbootinit.utils.ImageUploadUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Date;

@Service
public class CityDetailServiceImpl extends ServiceImpl<CityDetailMapper, CityDetail> implements CityDetailService {
    @Resource
    private RedisTemplate redisTemplate;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CityDetailVo getCityDetail(String cityName) {
        if (cityName.isEmpty()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        cityName = cityName.toLowerCase();
        Long areaId = (Long) redisTemplate.opsForValue().get(cityName);
        if (areaId==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"城市不存在请先创建城市");
        }
        CityDetailVo cityDetailVo1 = (CityDetailVo) redisTemplate.opsForValue().get(cityName + "_" + "detail");
        if (cityDetailVo1!=null){
            return cityDetailVo1;
        }
        CityDetail one = this.getOne(Wrappers.lambdaQuery(CityDetail.class).eq(CityDetail::getCityId, areaId));
        if (one==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"数据不存在");
        }
        CityDetailVo cityDetailVo = new CityDetailVo();
        BeanUtils.copyProperties(one,cityDetailVo);
        // 在数据库查询并转换 VO 后添加到 Redis
        redisTemplate.opsForValue().set(cityName + "_" + "detail", cityDetailVo, Duration.ofHours(12));
        return cityDetailVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdate(CityDetailDto cityDetailDto) throws Exception {
        if (cityDetailDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }
        if (cityDetailDto.getCityName() == null || cityDetailDto.getCityName().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "城市名称不能为空");
        }
        if (cityDetailDto.getCityIntroduce() == null || cityDetailDto.getCityIntroduce().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "城市介绍不能为空");
        }
        String lowerCase = cityDetailDto.getCityName().toLowerCase();
        Long areaId = (Long) redisTemplate.opsForValue().get(lowerCase);
        if (areaId==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"城市不存在请先创建城市");
        }
        CityDetail cityDetail = new CityDetail();
        BeanUtils.copyProperties(cityDetailDto, cityDetail);
        CityDetail one = this.getOne(Wrappers.lambdaQuery(CityDetail.class).eq(CityDetail::getCityName, lowerCase));
        if (one== null)
        {
            cityDetail.setCreateTime(new Date());
            cityDetail.setCityId(areaId);
           return this.save(cityDetail);
        }
        else
        {
            cityDetail.setCityDetailId(one.getCityDetailId());
            cityDetail.setCityId(one.getCityId());
            cityDetail.setUpdateDate(new Date());
           return this.updateById(cityDetail);
        }
    }
}
