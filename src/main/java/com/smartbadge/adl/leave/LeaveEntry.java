package com.smartbadge.adl.leave;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeaveEntry {
	private String referenceId;
	private LeaveType leaveType;
	private LeaveStatus leaveStatus;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate fromDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate toDate;

	private String reason;
	private String comments;
}
