package com.smartbadge.adl.staff.dto;

import java.util.List;

import com.smartbadge.adl.staff.AddressDto;
import com.smartbadge.adl.staff.BusinessCardType;
import com.smartbadge.adl.staff.Title;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStaffRequest {

    @NotBlank(message = "Staff ID is required")
    @Pattern(regexp = "^[A-Za-z][0-9]+$", message = "Staff ID must start with a letter followed by digits (e.g. E4861934)")
    private String staffId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    private String email;

    @NotNull(message = "Title is required")
    private Title title;

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    @NotBlank(message = "Grade is required")
    private String grade;

    @NotNull(message = "Business card type is required")
    private BusinessCardType businessCardType;

    @Valid
    private List<AddressDto> addresses;
}
