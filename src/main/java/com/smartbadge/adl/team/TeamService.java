package com.smartbadge.adl.team;

import com.smartbadge.adl.shared.exception.DuplicateResourceException;
import com.smartbadge.adl.shared.exception.ResourceNotFoundException;
import com.smartbadge.adl.team.dto.AddTeamMemberRequest;
import com.smartbadge.adl.team.dto.UpdateTeamMemberRequest;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;

    // ── Called by Staff module ────────────────────────────────────────────────
    public Optional<TeamDocumentDto> findByStaffId(String staffId) {
        log.info("Finding team document by staffId: {}", staffId);
        Optional<TeamDocumentDto> teamDocument = teamRepository.findByStaffId(staffId).map(this::toDto);
        log.info("Team document lookup completed for staffId: {}, found={}", staffId, teamDocument.isPresent());
        return teamDocument;
    }

    // ── REST endpoints ────────────────────────────────────────────────────────
    public TeamDocumentDto getByStaffId(String staffId) {
        log.info("Getting team document by staffId: {}", staffId);
        TeamDocumentDto dto = teamRepository.findByStaffId(staffId)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Team Document", staffId));
        log.info("Got team document by staffId: {}, memberCount={}", staffId, dto.getTeamMembers().size());
        return dto;
    }

    public TeamDocumentDto createTeam(String staffId) {
        log.info("Creating team document for staff: {}", staffId);
        if (teamRepository.existsByStaffId(staffId)) {
            log.warn("Create team document rejected because staffId already exists: {}", staffId);
            throw new DuplicateResourceException("Team Document", staffId);
        }
        TeamDocument doc = TeamDocument.builder().staffId(staffId).teamMembers(new ArrayList<>()).build();
        TeamDocumentDto dto = toDto(teamRepository.save(doc));
        log.info("Created team document for staff: {}, memberCount={}", staffId, dto.getTeamMembers().size());
        return dto;
    }

    public TeamDocumentDto addMember(String staffId, AddTeamMemberRequest req) {
        log.info("Adding team member {} for staff: {}", req.getStaffId(), staffId);
        TeamDocument doc = teamRepository.findByStaffId(staffId)
                .orElse(TeamDocument.builder().staffId(staffId).teamMembers(new ArrayList<>()).build());
        boolean exists = doc.getTeamMembers().stream()
                .anyMatch(m -> m.getStaffId().equals(req.getStaffId()));
        if (exists) {
            log.warn("Add team member rejected because member already exists: staffId={}, memberId={}", staffId,
                    req.getStaffId());
            throw new DuplicateResourceException("Team Member", req.getStaffId());
        }
        doc.getTeamMembers().add(modelMapper.map(req, TeamMember.class));
        TeamDocumentDto dto = toDto(teamRepository.save(doc));
        log.info("Added team member {} for staff: {}, memberCount={}", req.getStaffId(), staffId,
                dto.getTeamMembers().size());
        return dto;
    }

    public TeamDocumentDto updateMember(String staffId, String memberId, UpdateTeamMemberRequest req) {
        log.info("Updating team member {} for staff: {}", memberId, staffId);
        TeamDocument doc = getDocOrThrow(staffId);
        TeamMember member = doc.getTeamMembers().stream()
                .filter(m -> m.getStaffId().equals(memberId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Team Member", memberId));
        if (req.getName()   != null) member.setName(req.getName());
        if (req.getStatus() != null) member.setStatus(req.getStatus());
        if (req.getRole()   != null) member.setRole(req.getRole());
        TeamDocumentDto dto = toDto(teamRepository.save(doc));
        log.info("Updated team member {} for staff: {}, status={}", memberId, staffId, member.getStatus());
        return dto;
    }

    public TeamDocumentDto removeMember(String staffId, String memberId) {
        log.info("Removing team member {} for staff: {}", memberId, staffId);
        TeamDocument doc = getDocOrThrow(staffId);
        boolean removed = doc.getTeamMembers().removeIf(m -> m.getStaffId().equals(memberId));
        if (!removed) {
            log.warn("Remove team member rejected because member was not found: staffId={}, memberId={}", staffId,
                    memberId);
            throw new ResourceNotFoundException("Team Member", memberId);
        }
        TeamDocumentDto dto = toDto(teamRepository.save(doc));
        log.info("Removed team member {} for staff: {}, memberCount={}", memberId, staffId,
                dto.getTeamMembers().size());
        return dto;
    }

    // ── Mapping ───────────────────────────────────────────────────────────────
    private TeamDocument getDocOrThrow(String staffId) {
        log.info("Loading team document for staff: {}", staffId);
        return teamRepository.findByStaffId(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Team Document", staffId));
    }

    TeamDocumentDto toDto(TeamDocument doc) {
        log.trace("Mapping TeamDocument to TeamDocumentDto for staff: {}", doc.getStaffId());
        return modelMapper.map(doc, TeamDocumentDto.class);
    }
}
