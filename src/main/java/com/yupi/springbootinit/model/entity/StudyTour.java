package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Study Tour Application Form Entity Class
 */
@Data
public class StudyTour implements Serializable {

    private static final long serialVersionUID = 1L; // Serialization version number to ensure object transmission compatibility

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * Applicant's name
     * Validation rules: required, length 1-50 characters (to avoid too short/long invalid values)
     */
    @NotBlank(message = "Applicant name cannot be empty")
    @Size(min = 1, max = 50, message = "Applicant name length must be between 1-50 characters")
    private String name;

    /**
     * Email address
     * Validation rules: required, valid email format (to avoid invalid emails)
     */
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please enter a valid email format (e.g., xxx@xxx.com)")
    private String email;

    /**
     * Phone number
     * Validation rules: required, matches phone number format (supports common international phone numbers, regex can be adjusted based on business needs)
     */
    @NotBlank(message = "Phone number cannot be empty")
    @Size(max = 20, message = "Phone number must be less than 20 characters")
    private String phone;

    /**
     * Organization/Institution
     * Validation rules: required, length 1-100 characters (covers company, school, organization names)
     */
    @NotBlank(message = "Organization cannot be empty")
    @Size(min = 1, max = 100, message = "Organization name length must be between 1-100 characters")
    private String organization;

    /**
     * Group size
     * Value range: small(1-10 people), medium(11-30 people), large(31-50 people), xlarge(50+ people)
     * Validation rules: required (must select from dropdown options)
     */
    @NotBlank(message = "Please select group size")
    private String groupSize;

    /**
     * Requirements description
     * Validation rules: required, length 10-2000 characters (to avoid empty descriptions or overly long content)
     */
    @NotBlank(message = "Requirements description cannot be empty")
    @Size(min = 10, max = 2000, message = "Requirements description must be detailed, length between 10-2000 characters")
    private String message;

    private Date applyTime; // Application submission time
    private Date updateTime; // Update time
    private String formStatus; // Form status (e.g., pending, approved, rejected)
}
