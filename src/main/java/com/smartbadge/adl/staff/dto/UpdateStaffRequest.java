package com.smartbadge.adl.staff.dto;

import com.smartbadge.adl.staff.BusinessCardType;
import com.smartbadge.adl.staff.Title;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStaffRequest {
    private String name;

    @Email(message = "Must be a valid email address")
    private String email;

    private Title title;
    private String jobTitle;
    private String grade;
    private BusinessCardType businessCardType;
}
