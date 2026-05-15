package com.smartbadge.adl.staff;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.smartbadge.adl.leave.LeaveDocumentDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StaffDto {
    private String staffId;
    private String name;
    private String email;
    private Title title;
    private String jobTitle;
    private String grade;
    private BusinessCardType businessCardType;
    private LeaveDocumentDto leave;
    private List<AddressDto> address;
}
