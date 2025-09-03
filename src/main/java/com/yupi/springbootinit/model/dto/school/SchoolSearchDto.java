package com.yupi.springbootinit.model.dto.school;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class SchoolSearchDto  implements Serializable {
    private Long pageName;
    private Long pageSize;
    private List<String> searchCities;
    private List<String> searchPopularCities;
    private List<String> searchPrograms;
    private List<String> searchUniversityName;

}
