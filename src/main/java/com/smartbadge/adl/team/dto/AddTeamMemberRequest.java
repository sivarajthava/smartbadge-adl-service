package com.smartbadge.adl.team.dto;

import com.smartbadge.adl.team.MemberStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddTeamMemberRequest {

    @NotBlank(message = "Member staff ID is required")
    private String staffId;

    @NotBlank(message = "Member name is required")
    private String name;

    @NotNull(message = "Member status is required")
    private MemberStatus status;

    @NotBlank(message = "Role is required")
    private String role;
}
