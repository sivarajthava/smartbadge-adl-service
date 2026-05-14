package com.smartbadge.adl.leave.dto;

import com.smartbadge.adl.leave.LeaveStatus;
import com.smartbadge.adl.leave.LeaveType;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLeaveEntryRequest {
    private LeaveType leaveType;
    private LeaveStatus leaveStatus;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fromDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate toDate;

    private String reason;
    private String comments;
}
