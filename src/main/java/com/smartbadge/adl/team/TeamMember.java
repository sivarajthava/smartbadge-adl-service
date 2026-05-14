package com.smartbadge.adl.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamMember {
    private String staffId;
    private String name;
    private MemberStatus status;
    private String role;
}
