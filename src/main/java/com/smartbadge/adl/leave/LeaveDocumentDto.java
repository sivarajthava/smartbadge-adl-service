package com.smartbadge.adl.leave;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveDocumentDto {
    private Integer leaveBalance;
    private String staffId;
    private List<LeaveEntryDto> leaves;
}
