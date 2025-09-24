package com.yupi.springbootinit.controller;

import com.yupi.springbootinit.annotation.IpWhitelist;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.model.entity.Area;
import com.yupi.springbootinit.model.vo.AreaVo;
import com.yupi.springbootinit.service.AreaService;
import com.yupi.springbootinit.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/area")
public class AreaController {
    @Autowired
    private AreaService areaService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/get")
    public BaseResponse< ArrayList<AreaVo>> getArea() {
        //获取所有地区信息
        List<Area> list = areaService.list();
        ArrayList<AreaVo> areaVos = new ArrayList<>();
        list.forEach(item -> {
            AreaVo areaVo = new AreaVo();
            areaVo.setAreaName(item.getAreaName());
            areaVo.setAreaInitial(item.getAreaInitial());
            areaVo.setCreateTime(item.getCreateTime());
            areaVos.add(areaVo);
        });
        return ResultUtils.success(areaVos);
    }
    /**
     * 创建地区
     */
    @PostMapping("/create")
    @IpWhitelist({"144.34.224.28"})
    public BaseResponse<String> createArea(@RequestParam String areaName ) {
        if (areaName != null && !areaName.isEmpty()) {
            schoolService.list();
            // 校验是否全为字母
            if (!areaName.matches("^[a-zA-Z]+$")) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "地区名称必须只包含字母");
            }
            // 检查 key 是否存在
            if (redisTemplate.hasKey(areaName)) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "地区存在");
            }
            char firstChar = areaName.charAt(0);
            String firstLetter = String.valueOf(Character.toUpperCase(firstChar));
            areaName=areaName.toLowerCase();
            // 使用 firstLetter 变量作为首字母大写的结果
            Date date = new Date();
            Area area = new Area();
            area.setAreaName(areaName);
            area.setCreateTime(date);
            area.setAreaInitial(firstLetter);//添加首字母，首页面要大写
            boolean save = areaService.save(area);
            if (save) {
               redisTemplate.opsForValue().set(area.getAreaName(), area.getAreaId());
            }
            return ResultUtils.success("添加成功");
        }
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数为空");
    }

    /**
     * 根据ID删除地区（物理删除）
     */
    @GetMapping("/remove")
    public BaseResponse<String>  deleteArea(@RequestParam String areaName) {
        if (!redisTemplate.hasKey(areaName)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "地区不存在");
        }
        if (areaName==null||areaName.isEmpty()){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
       Long id = (long) redisTemplate.opsForValue().get(areaName);
        log.info("删除地区ID：{}",id);
        boolean result = areaService.removeById(id);
        if (result){
            redisTemplate.delete(areaName);
            return ResultUtils.success("删除成功");
        }{
            throw new RuntimeException("删除失败");
        }

    }

}
