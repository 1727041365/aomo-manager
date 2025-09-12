package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.model.dto.school.SchoolInfoDTO;
import com.yupi.springbootinit.model.entity.SchoolDetail;
import com.yupi.springbootinit.service.SchoolDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/ProgramsDetail")
public class SchoolDetailController {

    @Resource
    private SchoolDetailService schoolDetailService;
    @Resource
    private RedisTemplate redisTemplate;
    public record Introduction(Long id, String schoolIntroduction) {}
    public record NonDegree(Long id, String schoolNonDegree) {}
    public record BachelorDegree(Long id, String schoolBachelorDegree) {}
    public record MasterDegree(Long id, String schoolMasterDegree) {}
    public record Phd(Long id, String schoolPhd) {}
    public record Scenery(Long id, String schoolScenery) {}
    @PostMapping("/create")
    public BaseResponse<String> create(@RequestBody SchoolInfoDTO schoolInfoDTO) throws Exception {
        Long schoolId = schoolInfoDTO.getSchoolId();
        if (schoolId==null||schoolId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"id参数为空");
        }
        if (schoolInfoDTO.getSchoolLogo() == null || schoolInfoDTO.getSchoolLogo().isEmpty()) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "logo没有上传");
        }
        if (schoolInfoDTO.getSchoolIntroduction()==null||schoolInfoDTO.getSchoolIntroduction().isEmpty()){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"学校介绍没有上传");
        }
        if (schoolInfoDTO.getSchoolScenery() == null || schoolInfoDTO.getSchoolScenery().isEmpty()) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "学校风景没有上传");
        }
        Boolean result = schoolDetailService.saveDetail(schoolInfoDTO);
        if ( result){
            return ResultUtils.success("创建成功");
        }else {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR,"创建失败");
        }
    }
    @GetMapping("/get")
    public BaseResponse<SchoolDetail> get(@RequestParam("schoolId") Long schoolId) {
        if (schoolId==null||schoolId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"id参数为空");
        }
        SchoolDetail schoolDetail = schoolDetailService.getOne(Wrappers.lambdaQuery(SchoolDetail.class).eq(SchoolDetail::getSchoolId, schoolId));
        return ResultUtils.success(schoolDetail);
    }
    @GetMapping("/getIntroduction")
    public BaseResponse<Introduction > getSchoolIntroduction(@RequestParam("schoolId") Long schoolId) {
        if (schoolId==null||schoolId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"id参数为空");
        }
        if (redisTemplate.opsForValue().get("introduction:"+schoolId)!=null){
            return ResultUtils.success((Introduction) redisTemplate.opsForValue().get("introduction:"+schoolId));
        }
        LambdaQueryWrapper<SchoolDetail> eq = Wrappers.lambdaQuery(SchoolDetail.class)
                .select(SchoolDetail::getSchoolIntroduction)
                .eq(SchoolDetail::getSchoolId, schoolId);
        SchoolDetail one = schoolDetailService.getOne(eq);
        if (one==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"不存在改数据,id有误");
        }
        Introduction introduction = new Introduction(schoolId, one.getSchoolIntroduction());
        redisTemplate.opsForValue().set("introduction:"+schoolId,introduction, 300, TimeUnit.MINUTES);
        return ResultUtils.success(introduction);
    }
    @GetMapping("/getNonDegree")
    public BaseResponse<NonDegree> getNonDegree(@RequestParam("schoolId") Long schoolId) {
        if (schoolId==null||schoolId<=0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"id参数为空");
        }
        if (redisTemplate.opsForValue().get("nonDegree:"+schoolId)!=null){
            return ResultUtils.success((NonDegree) redisTemplate.opsForValue().get("nonDegree:"+schoolId));
        }
        LambdaQueryWrapper<SchoolDetail> eq = Wrappers.lambdaQuery(SchoolDetail.class)
                .select(SchoolDetail::getChineseLanguageProgram)
                .eq(SchoolDetail::getSchoolId, schoolId);
        SchoolDetail one = schoolDetailService.getOne(eq);
        if (one==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"不存在改数据,id有误");
        }
        NonDegree nonDegree = new NonDegree(schoolId, one.getChineseLanguageProgram());
        redisTemplate.opsForValue().set("nonDegree:"+schoolId,nonDegree,300, TimeUnit.MINUTES);
        return ResultUtils.success(nonDegree);
    }
    @GetMapping("/getBachelorDegree")
    public BaseResponse<BachelorDegree> getBachelorDegree(@RequestParam("schoolId") Long schoolId) {
        if (schoolId == null || schoolId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "id参数为空");
        }
        String redisKey = "bachelorDegree:" + schoolId;
        if (redisTemplate.opsForValue().get(redisKey) != null) {
            return ResultUtils.success((BachelorDegree) redisTemplate.opsForValue().get(redisKey));
        }
        LambdaQueryWrapper<SchoolDetail> queryWrapper = Wrappers.lambdaQuery(SchoolDetail.class)
                .select(SchoolDetail::getBachelorDegree)
                .eq(SchoolDetail::getSchoolId, schoolId);
        SchoolDetail one = schoolDetailService.getOne(queryWrapper);
        if (one == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "不存在该数据,id有误");
        }
        BachelorDegree bachelorDegree = new BachelorDegree(schoolId, one.getBachelorDegree());
        redisTemplate.opsForValue().set(redisKey, bachelorDegree, 300, TimeUnit.MINUTES);
        return ResultUtils.success(bachelorDegree);
    }

    @GetMapping("/getMasterDegree")
    public BaseResponse<MasterDegree> getMasterDegree(@RequestParam("schoolId") Long schoolId) {
        if (schoolId == null || schoolId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "id参数为空");
        }
        String redisKey = "masterDegree:" + schoolId;
        if (redisTemplate.opsForValue().get(redisKey) != null) {
            return ResultUtils.success((MasterDegree) redisTemplate.opsForValue().get(redisKey));
        }
        LambdaQueryWrapper<SchoolDetail> queryWrapper = Wrappers.lambdaQuery(SchoolDetail.class)
                .select(SchoolDetail::getMasterDegree)
                .eq(SchoolDetail::getSchoolId, schoolId);
        SchoolDetail one = schoolDetailService.getOne(queryWrapper);
        if (one == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "不存在该数据,id有误");
        }
        MasterDegree masterDegree = new MasterDegree(schoolId, one.getMasterDegree());
        redisTemplate.opsForValue().set(redisKey, masterDegree, 300, TimeUnit.MINUTES);
        return ResultUtils.success(masterDegree);
    }

    @GetMapping("/getPhd")
    public BaseResponse<Phd> getPhd(@RequestParam("schoolId") Long schoolId) {
        if (schoolId == null || schoolId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "id参数为空");
        }
        String redisKey = "phd:" + schoolId;
        if (redisTemplate.opsForValue().get(redisKey) != null) {
            return ResultUtils.success((Phd) redisTemplate.opsForValue().get(redisKey));
        }
        LambdaQueryWrapper<SchoolDetail> queryWrapper = Wrappers.lambdaQuery(SchoolDetail.class)
                .select(SchoolDetail::getPhd)
                .eq(SchoolDetail::getSchoolId, schoolId);
        SchoolDetail one = schoolDetailService.getOne(queryWrapper);
        if (one == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "不存在该数据,id有误");
        }
        Phd phd = new Phd(schoolId, one.getPhd());
        redisTemplate.opsForValue().set(redisKey, phd, 300, TimeUnit.MINUTES);
        return ResultUtils.success(phd);
    }

    @GetMapping("/getScenery")
    public BaseResponse<Scenery> getScenery(@RequestParam("schoolId") Long schoolId) {
        if (schoolId == null || schoolId <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "id参数为空");
        }
        String redisKey = "scenery:" + schoolId;
        if (redisTemplate.opsForValue().get(redisKey) != null) {
            return ResultUtils.success((Scenery) redisTemplate.opsForValue().get(redisKey));
        }
        LambdaQueryWrapper<SchoolDetail> queryWrapper = Wrappers.lambdaQuery(SchoolDetail.class)
                .select(SchoolDetail::getSchoolScenery)
                .eq(SchoolDetail::getSchoolId, schoolId);
        SchoolDetail one = schoolDetailService.getOne(queryWrapper);
        if (one == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "不存在该数据,id有误");
        }
        Scenery scenery = new Scenery(schoolId, one.getSchoolScenery());
        redisTemplate.opsForValue().set(redisKey, scenery, 300, TimeUnit.MINUTES);
        return ResultUtils.success(scenery);
    }

}
