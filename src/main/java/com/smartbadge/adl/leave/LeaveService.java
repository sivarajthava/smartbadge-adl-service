package com.smartbadge.adl.leave;

import com.smartbadge.adl.leave.dto.CreateLeaveEntryRequest;
import com.smartbadge.adl.leave.dto.UpdateLeaveEntryRequest;
import com.smartbadge.adl.shared.exception.ResourceNotFoundException;


import java.util.ArrayList;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final ModelMapper modelMapper;

    // ── Called by Staff module ────────────────────────────────────────────────
    public Optional<LeaveDocumentDto> findByStaffId(String staffId) {
        log.info("Finding leave document by staffId: {}", staffId);
        Optional<LeaveDocumentDto> leaveDocument = leaveRepository.findByStaffId(staffId).map(this::toDto);
        log.info("Leave document lookup completed for staffId: {}, found={}", staffId, leaveDocument.isPresent());
        return leaveDocument;
    }

    // ── REST endpoints ────────────────────────────────────────────────────────
    public LeaveDocumentDto getByStaffId(String staffId) {
        log.info("Getting leave document by staffId: {}", staffId);
        LeaveDocumentDto dto = leaveRepository.findByStaffId(staffId)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Leave Document", staffId));
        log.info("Got leave document by staffId: {}, leaveCount={}", staffId, dto.getLeaves().size());
        return dto;
    }

    public LeaveDocumentDto initializeOrUpdate(String staffId, Integer leaveBalance) {
        log.info("Initializing or updating leave document for staff: {}, requestedBalance={}", staffId, leaveBalance);
        LeaveDocument doc = leaveRepository.findByStaffId(staffId)
                .orElse(LeaveDocument.builder().staffId(staffId).leaves(new ArrayList<>()).build());
        doc.setLeaveBalance(leaveBalance != null ? leaveBalance : doc.getLeaveBalance());
        LeaveDocumentDto dto = toDto(leaveRepository.save(doc));
        log.info("Initialized or updated leave document for staff: {}, leaveBalance={}, leaveCount={}", staffId,
                dto.getLeaveBalance(), dto.getLeaves().size());
        return dto;
    }

    public LeaveDocumentDto updateBalance(String staffId, Integer leaveBalance) {
        log.info("Updating leave balance for staff: {}, leaveBalance={}", staffId, leaveBalance);
        LeaveDocument doc = getDocOrThrow(staffId);
        doc.setLeaveBalance(leaveBalance);
        LeaveDocumentDto dto = toDto(leaveRepository.save(doc));
        log.info("Updated leave balance for staff: {}, leaveBalance={}", staffId, dto.getLeaveBalance());
        return dto;
    }

    public LeaveDocumentDto addLeaveEntry(String staffId, CreateLeaveEntryRequest req) {
        log.info("Adding leave entry for staff: {}, leaveType={}", staffId, req.getLeaveType());
        LeaveDocument doc = leaveRepository.findByStaffId(staffId)
                .orElse(LeaveDocument.builder().staffId(staffId).leaveBalance(0)
                        .leaves(new ArrayList<>()).build());
        doc.setLeaveBalance(req.getLeaveBalance() != null ? req.getLeaveBalance() : doc.getLeaveBalance());
        String refId = "LEV-REQ-" + System.currentTimeMillis();
        LeaveEntry entry = modelMapper.map(req, LeaveEntry.class);
        entry.setReferenceId(refId);
        entry.setLeaveStatus(LeaveStatus.PENDING);
        doc.getLeaves().add(entry);
        LeaveDocumentDto dto = toDto(leaveRepository.save(doc));
        log.info("Added leave entry {} for staff: {}, leaveCount={}", refId, staffId, dto.getLeaves().size());
        return dto;
    }

    public LeaveDocumentDto updateLeaveEntry(String staffId, String referenceId, UpdateLeaveEntryRequest req) {
        log.info("Updating leave entry {} for staff: {}", referenceId, staffId);
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
        LeaveDocumentDto dto = toDto(leaveRepository.save(doc));
        log.info("Updated leave entry {} for staff: {}, status={}", referenceId, staffId, entry.getLeaveStatus());
        return dto;
    }

    public LeaveDocumentDto deleteLeaveEntry(String staffId, String referenceId) {
        log.info("Deleting leave entry {} for staff: {}", referenceId, staffId);
        LeaveDocument doc = getDocOrThrow(staffId);
        boolean removed = doc.getLeaves().removeIf(e -> e.getReferenceId().equals(referenceId));
        if (!removed) {
            log.warn("Delete leave entry rejected because reference was not found: staffId={}, referenceId={}", staffId,
                    referenceId);
            throw new ResourceNotFoundException("Leave Entry", referenceId);
        }
        LeaveDocumentDto dto = toDto(leaveRepository.save(doc));
        log.info("Deleted leave entry {} for staff: {}, leaveCount={}", referenceId, staffId, dto.getLeaves().size());
        return dto;
    }

    // ── Mapping ───────────────────────────────────────────────────────────────
    private LeaveDocument getDocOrThrow(String staffId) {
        log.info("Loading leave document for staff: {}", staffId);
        return leaveRepository.findByStaffId(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave Document", staffId));
    }

    LeaveDocumentDto toDto(LeaveDocument doc) {
        log.trace("Mapping LeaveDocument to LeaveDocumentDto for staff: {}", doc.getStaffId());
        return modelMapper.map(doc, LeaveDocumentDto.class);
    }
}
