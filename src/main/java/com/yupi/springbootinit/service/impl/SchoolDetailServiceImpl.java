package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.mapper.SchoolDetailMapper;
import com.yupi.springbootinit.model.dto.school.SchoolInfoDTO;
import com.yupi.springbootinit.model.entity.SchoolDetail;
import com.yupi.springbootinit.service.SchoolDetailService;
import com.yupi.springbootinit.utils.ImageUploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class SchoolDetailServiceImpl extends ServiceImpl<SchoolDetailMapper, SchoolDetail> implements SchoolDetailService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveDetail(SchoolInfoDTO schoolInfoDTO) throws Exception {
        SchoolDetail schoolDetail = new SchoolDetail();
        schoolDetail.setSchoolId(schoolInfoDTO.getSchoolId());
        if (schoolInfoDTO.getSchoolLogo() != null && !schoolInfoDTO.getSchoolLogo().isEmpty()){
            schoolDetail.setSchoolLogo(schoolInfoDTO.getSchoolLogo());
        }
        boolean empty = schoolInfoDTO.getSchoolScenery().isEmpty();
        if (!empty)
        {
            String scenery = schoolInfoDTO.getSceneryVideo()+","+ImageUploadUtil.parseAndSaveImages(schoolInfoDTO.getSchoolScenery());
            schoolDetail.setSchoolScenery(scenery);
        }
        boolean empty1 = schoolInfoDTO.getSchoolIntroduction().isEmpty();
        if (!empty1){
            String introduction = ImageUploadUtil.parseAndSaveImages(schoolInfoDTO.getSchoolIntroduction());
            schoolDetail.setSchoolIntroduction(introduction);
        }
        schoolDetail.setCreateTime(new Date());
        schoolDetail.setUpdateTime(new Date());
        if (schoolInfoDTO.getBachelorDegree() != null && !schoolInfoDTO.getBachelorDegree().isEmpty()) {
            String bachelorDegree = ImageUploadUtil.parseAndSaveImages(schoolInfoDTO.getBachelorDegree());
            schoolDetail.setBachelorDegree(bachelorDegree);
        }
        if (schoolInfoDTO.getMasterDegree() != null && !schoolInfoDTO.getMasterDegree().isEmpty()) {
            String masterDegree= ImageUploadUtil.parseAndSaveImages(schoolInfoDTO.getMasterDegree());
            schoolDetail.setMasterDegree(masterDegree);
        }
        if (schoolInfoDTO.getPhd() != null && !schoolInfoDTO.getPhd().isEmpty()) {
            String phd = ImageUploadUtil.parseAndSaveImages(schoolInfoDTO.getPhd());
            schoolDetail.setPhd(phd);
        }
        if (schoolInfoDTO.getChineseLanguageProgram() != null && !schoolInfoDTO.getChineseLanguageProgram().isEmpty()){
            String languageProgram = ImageUploadUtil.parseAndSaveImages(schoolInfoDTO.getChineseLanguageProgram());
            schoolDetail.setChineseLanguageProgram(languageProgram);
        }
        SchoolDetail one = this.getOne(Wrappers.lambdaQuery(SchoolDetail.class).eq(SchoolDetail::getSchoolId, schoolInfoDTO.getSchoolId()));
        if (one != null){
           schoolDetail.setSchoolDetailId(one.getSchoolDetailId());
           schoolDetail.setUpdateTime(new Date());
           return this.updateById(schoolDetail);
        }else {
            return this.save(schoolDetail);
        }
    }
}
