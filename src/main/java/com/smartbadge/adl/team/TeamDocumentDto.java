package com.smartbadge.adl.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamDocumentDto {
    private String staffId;
    private List<TeamMemberDto> teamMembers;
}
