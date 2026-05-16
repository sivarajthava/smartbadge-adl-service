package com.smartbadge.adl.team;

import com.smartbadge.adl.shared.ApiResponse;
import com.smartbadge.adl.team.dto.AddTeamMemberRequest;
import com.smartbadge.adl.team.dto.UpdateTeamMemberRequest;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/team")
@RequiredArgsConstructor
@Tag(name = "Team Management", description = "Manage team documents and members")
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/{staffId}")
    @Operation(summary = "Get team document for a staff member")
    public ResponseEntity<ApiResponse<TeamDocumentDto>> get(@PathVariable String staffId) {
        return ResponseEntity.ok(ApiResponse.success(teamService.getByStaffId(staffId)));
    }

    @PostMapping("/{staffId}")
    @Operation(summary = "Initialize team document for a staff member")
    public ResponseEntity<ApiResponse<TeamDocumentDto>> create(@PathVariable String staffId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(teamService.createTeam(staffId)));
    }

    @PostMapping("/{staffId}/members")
    @Operation(summary = "Add a member to the team")
    public ResponseEntity<ApiResponse<TeamDocumentDto>> addMember(
            @PathVariable String staffId,
            @Valid @RequestBody AddTeamMemberRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(teamService.addMember(staffId, req)));
    }

    @PutMapping("/{staffId}/members/{memberId}")
    @Operation(summary = "Update a team member (partial update)")
    public ResponseEntity<ApiResponse<TeamDocumentDto>> updateMember(
            @PathVariable String staffId,
            @PathVariable String memberId,
            @RequestBody UpdateTeamMemberRequest req) {
        return ResponseEntity.ok(ApiResponse.success(teamService.updateMember(staffId, memberId, req)));
    }

    @DeleteMapping("/{staffId}/members/{memberId}")
    @Operation(summary = "Remove a member from the team")
    public ResponseEntity<ApiResponse<TeamDocumentDto>> removeMember(
            @PathVariable String staffId,
            @PathVariable String memberId) {
        return ResponseEntity.ok(ApiResponse.success(teamService.removeMember(staffId, memberId)));
    }
}
