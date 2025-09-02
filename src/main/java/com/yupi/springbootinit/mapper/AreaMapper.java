package com.yupi.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupi.springbootinit.model.entity.Area;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地区Mapper接口
 */
@Mapper
public interface AreaMapper extends BaseMapper<Area> {
}
