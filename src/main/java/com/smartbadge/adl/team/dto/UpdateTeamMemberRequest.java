package com.smartbadge.adl.team.dto;

import com.smartbadge.adl.team.MemberStatus;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeamMemberRequest {
    private String name;
    private MemberStatus status;
    private String role;
}
