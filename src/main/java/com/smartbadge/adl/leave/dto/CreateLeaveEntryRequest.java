package com.smartbadge.adl.leave.dto;

import com.smartbadge.adl.leave.LeaveType;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLeaveEntryRequest {

    @NotNull(message = "Leave type is required")
    private LeaveType leaveType;

    @NotNull(message = "From date is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fromDate;

    @NotNull(message = "To date is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate toDate;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String comments;
}
