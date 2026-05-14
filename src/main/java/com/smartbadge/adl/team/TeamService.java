package com.smartbadge.adl.team;

import com.smartbadge.adl.shared.exception.DuplicateResourceException;
import com.smartbadge.adl.shared.exception.ResourceNotFoundException;
import com.smartbadge.adl.staff.Staff;
import com.smartbadge.adl.team.dto.AddTeamMemberRequest;
import com.smartbadge.adl.team.dto.UpdateTeamMemberRequest;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    // ── Called by Staff module ────────────────────────────────────────────────
    public Optional<TeamDocumentDto> findByStaffId(String staffId) {
        return teamRepository.findByStaffId(staffId).map(this::toDto);
    }

    // ── REST endpoints ────────────────────────────────────────────────────────
    public TeamDocumentDto getByStaffId(String staffId) {
        return teamRepository.findByStaffId(staffId)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Team Document", staffId));
    }

    public TeamDocumentDto createTeam(String staffId) {
        if (teamRepository.existsByStaffId(staffId)) {
            throw new DuplicateResourceException("Team Document", staffId);
        }
        TeamDocument doc = TeamDocument.builder().staffId(staffId).teamMembers(new ArrayList<>()).build();
        log.info("Created team document for staff: {}", staffId);
        return toDto(teamRepository.save(doc));
    }

    public TeamDocumentDto addMember(String staffId, AddTeamMemberRequest req) {
        TeamDocument doc = teamRepository.findByStaffId(staffId)
                .orElse(TeamDocument.builder().staffId(staffId).teamMembers(new ArrayList<>()).build());
        boolean exists = doc.getTeamMembers().stream()
                .anyMatch(m -> m.getStaffId().equals(req.getStaffId()));
        if (exists) throw new DuplicateResourceException("Team Member", req.getStaffId());
        doc.getTeamMembers().add(TeamMember.builder()
                .staffId(req.getStaffId()).name(req.getName())
                .status(req.getStatus()).role(req.getRole()).build());
        return toDto(teamRepository.save(doc));
    }

    public TeamDocumentDto updateMember(String staffId, String memberId, UpdateTeamMemberRequest req) {
        TeamDocument doc = getDocOrThrow(staffId);
        TeamMember member = doc.getTeamMembers().stream()
                .filter(m -> m.getStaffId().equals(memberId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Team Member", memberId));
        if (req.getName()   != null) member.setName(req.getName());
        if (req.getStatus() != null) member.setStatus(req.getStatus());
        if (req.getRole()   != null) member.setRole(req.getRole());
        return toDto(teamRepository.save(doc));
    }

    public TeamDocumentDto removeMember(String staffId, String memberId) {
        TeamDocument doc = getDocOrThrow(staffId);
        boolean removed = doc.getTeamMembers().removeIf(m -> m.getStaffId().equals(memberId));
        if (!removed) throw new ResourceNotFoundException("Team Member", memberId);
        return toDto(teamRepository.save(doc));
    }

    // ── Mapping ───────────────────────────────────────────────────────────────
    private TeamDocument getDocOrThrow(String staffId) {
        return teamRepository.findByStaffId(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Team Document", staffId));
    }

    TeamDocumentDto toDto(TeamDocument doc) {
        return TeamDocumentDto.builder()
                .staffId(doc.getStaffId())
                .teamMembers(doc.getTeamMembers().stream().map(this::toMemberDto).collect(Collectors.toList()))
                .build();
    }

    private TeamMemberDto toMemberDto(TeamMember m) {
        return TeamMemberDto.builder()
                .staffId(m.getStaffId()).name(m.getName())
                .status(m.getStatus()).role(m.getRole()).build();
    }
}
