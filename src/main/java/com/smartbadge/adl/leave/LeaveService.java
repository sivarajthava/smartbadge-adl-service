package com.smartbadge.adl.leave;

import com.smartbadge.adl.leave.dto.CreateLeaveEntryRequest;
import com.smartbadge.adl.leave.dto.UpdateLeaveEntryRequest;
import com.smartbadge.adl.shared.exception.ResourceNotFoundException;
import com.smartbadge.adl.staff.Staff;


import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRepository leaveRepository;

    // ── Called by Staff module ────────────────────────────────────────────────
    public Optional<LeaveDocumentDto> findByStaffId(String staffId) {
        return leaveRepository.findByStaffId(staffId).map(this::toDto);
    }

    // ── REST endpoints ────────────────────────────────────────────────────────
    public LeaveDocumentDto getByStaffId(String staffId) {
        return leaveRepository.findByStaffId(staffId)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Leave Document", staffId));
    }

    public LeaveDocumentDto initializeOrUpdate(String staffId, Integer leaveBalance) {
        LeaveDocument doc = leaveRepository.findByStaffId(staffId)
                .orElse(LeaveDocument.builder().staffId(staffId).leaves(new ArrayList<>()).build());
        doc.setLeaveBalance(leaveBalance != null ? leaveBalance : doc.getLeaveBalance());
        LeaveDocumentDto dto = toDto(leaveRepository.save(doc));
        log.info("Initialized/updated leave document for staff: {}", staffId);
        return dto;
    }

    public LeaveDocumentDto updateBalance(String staffId, Integer leaveBalance) {
        LeaveDocument doc = getDocOrThrow(staffId);
        doc.setLeaveBalance(leaveBalance);
        return toDto(leaveRepository.save(doc));
    }

    public LeaveDocumentDto addLeaveEntry(String staffId, CreateLeaveEntryRequest req) {
        LeaveDocument doc = leaveRepository.findByStaffId(staffId)
                .orElse(LeaveDocument.builder().staffId(staffId).leaveBalance(0)
                        .leaves(new ArrayList<>()).build());
        String refId = "LEV-REQ-" + System.currentTimeMillis();
        LeaveEntry entry = LeaveEntry.builder()
                .referenceId(refId)
                .leaveType(req.getLeaveType())
                .leaveStatus(LeaveStatus.PENDING)
                .fromDate(req.getFromDate())
                .toDate(req.getToDate())
                .reason(req.getReason())
                .comments(req.getComments())
                .build();
        doc.getLeaves().add(entry);
        log.info("Added leave entry {} for staff: {}", refId, staffId);
        return toDto(leaveRepository.save(doc));
    }

    public LeaveDocumentDto updateLeaveEntry(String staffId, String referenceId, UpdateLeaveEntryRequest req) {
        LeaveDocument doc = getDocOrThrow(staffId);
        LeaveEntry entry = doc.getLeaves().stream()
                .filter(e -> e.getReferenceId().equals(referenceId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Leave Entry", referenceId));
        if (req.getLeaveType()   != null) entry.setLeaveType(req.getLeaveType());
        if (req.getLeaveStatus() != null) entry.setLeaveStatus(req.getLeaveStatus());
        if (req.getFromDate()    != null) entry.setFromDate(req.getFromDate());
        if (req.getToDate()      != null) entry.setToDate(req.getToDate());
        if (req.getReason()      != null) entry.setReason(req.getReason());
        if (req.getComments()    != null) entry.setComments(req.getComments());
        return toDto(leaveRepository.save(doc));
    }

    public LeaveDocumentDto deleteLeaveEntry(String staffId, String referenceId) {
        LeaveDocument doc = getDocOrThrow(staffId);
        boolean removed = doc.getLeaves().removeIf(e -> e.getReferenceId().equals(referenceId));
        if (!removed) throw new ResourceNotFoundException("Leave Entry", referenceId);
        return toDto(leaveRepository.save(doc));
    }

    // ── Mapping ───────────────────────────────────────────────────────────────
    private LeaveDocument getDocOrThrow(String staffId) {
        return leaveRepository.findByStaffId(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave Document", staffId));
    }

    LeaveDocumentDto toDto(LeaveDocument doc) {
        return LeaveDocumentDto.builder()
                .staffId(doc.getStaffId())
                .leaveBalance(doc.getLeaveBalance())
                .leaves(doc.getLeaves().stream().map(this::toEntryDto).collect(Collectors.toList()))
                .build();
    }

    private LeaveEntryDto toEntryDto(LeaveEntry e) {
        return LeaveEntryDto.builder()
                .referenceId(e.getReferenceId()).leaveType(e.getLeaveType())
                .leaveStatus(e.getLeaveStatus()).fromDate(e.getFromDate())
                .toDate(e.getToDate()).reason(e.getReason()).comments(e.getComments())
                .build();
    }
}
