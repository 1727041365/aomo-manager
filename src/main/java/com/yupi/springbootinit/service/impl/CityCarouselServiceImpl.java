package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.mapper.CityCarouselMapper;
import com.yupi.springbootinit.model.dto.school.CityCarouselDto;
import com.yupi.springbootinit.model.entity.CityCarousel;
import com.yupi.springbootinit.model.vo.CityCarouselVo;
import com.yupi.springbootinit.service.CityCarouselService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityCarouselServiceImpl extends ServiceImpl<CityCarouselMapper, CityCarousel> implements CityCarouselService {
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<CityCarouselVo> getCityCarousel(String cityName) {
        if (cityName == null || cityName.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        cityName = cityName.toLowerCase();
        if (!redisTemplate.hasKey(cityName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "城市不存在请先创建城市");
        }
        List<CityCarouselVo> cityCarouselVoList = (List<CityCarouselVo>) redisTemplate.opsForValue().get(cityName + "_" + "carousel");
        if (cityCarouselVoList == null) {
            List<CityCarousel> one = this.list(Wrappers.lambdaQuery(CityCarousel.class).eq(CityCarousel::getCityName, cityName));
            if (one == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "轮播图数据不存在");
            }
            List<CityCarouselVo> collect = one.stream().map(cityCarousel -> {
                CityCarouselVo cityCarouselVo = new CityCarouselVo();
                BeanUtils.copyProperties(cityCarousel, cityCarouselVo);
                return cityCarouselVo;
            }).collect(Collectors.toList());
            redisTemplate.opsForValue().set(cityName + "_" + "carousel",  collect, Duration.ofHours(12));
            return  collect;
        } else {
            return cityCarouselVoList;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdate(CityCarouselDto cityCarouselDto) {
        if (cityCarouselDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }
        if (cityCarouselDto.getCityCarouselCategory() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类不能为空");
        }
        if (cityCarouselDto.getCarouselItems() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "轮播图数据不存在");
        }
//        cityCarouselDto.getCarouselItems().forEach(item -> {
//            try {
//                String imgUrl = ImageUploadUtil.parseAndSaveImages(item.getCityCarouselImg());
//                item.setCityCarouselImg(imgUrl);
//            } catch (Exception e) {
//                throw new BusinessException(ErrorCode.OPERATION_ERROR, "图片处理失败: " + e.getMessage());
//            }
//        });
        CityCarousel cityCarousel = new CityCarousel();
        BeanUtils.copyProperties(cityCarouselDto, cityCarousel);
//        List<CityCarousel.CarouselItem> carouselItems = cityCarousel.getCarouselItems();
//        cityCarouselDto.getCarouselItems().forEach(item -> {
//            carouselItems.add(item);
//        });
        String cityName = cityCarouselDto.getCityName().toLowerCase();
//        CityCarousel existing = this.getOne(Wrappers.lambdaQuery(CityCarousel.class)
//                .eq(CityCarousel::getCityName, cityName));
//        if (existing == null) {
            // 新增
            cityCarousel.setCreateTime(new Date());
            cityCarousel.setCityName(cityName);
            return this.save(cityCarousel);
//        } else {
//            // 更新
//            cityCarousel.setCityCarouselId(existing.getCityCarouselId()); // 设置主键
//            cityCarousel.setUpdateDate(new Date());
//            return this.updateById(cityCarousel);
//        }
    }
}
