package com.smartbadge.adl.leave.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLeaveBalanceRequest {

    @NotNull(message = "Leave balance is required")
    @Min(value = 0, message = "Leave balance cannot be negative")
    private Integer leaveBalance;
}
