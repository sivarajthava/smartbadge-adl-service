package com.smartbadge.adl.leave;

import com.smartbadge.adl.leave.dto.CreateLeaveEntryRequest;
import com.smartbadge.adl.leave.dto.UpdateLeaveBalanceRequest;
import com.smartbadge.adl.leave.dto.UpdateLeaveEntryRequest;
import com.smartbadge.adl.shared.ApiResponse;
import com.smartbadge.adl.staff.Staff;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/leave")
@RequiredArgsConstructor
@Tag(name = "Leave Management", description = "Manage staff leave documents and entries")
public class LeaveController {

    private final LeaveService leaveService;

    @GetMapping("/{staffId}")
    @Operation(summary = "Get leave document for a staff member")
    public ResponseEntity<ApiResponse<LeaveDocumentDto>> get(@PathVariable String staffId) {
        return ResponseEntity.ok(ApiResponse.success(leaveService.getByStaffId(staffId)));
    }

    @PostMapping("/{staffId}")
    @Operation(summary = "Initialize leave document (idempotent)")
    public ResponseEntity<ApiResponse<LeaveDocumentDto>> initialize(
            @PathVariable String staffId,
            @RequestParam(defaultValue = "0") Integer leaveBalance) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(leaveService.initializeOrUpdate(staffId, leaveBalance)));
    }

    @PatchMapping("/{staffId}/balance")
    @Operation(summary = "Update leave balance")
    public ResponseEntity<ApiResponse<LeaveDocumentDto>> updateBalance(
            @PathVariable String staffId,
            @Valid @RequestBody UpdateLeaveBalanceRequest req) {
        return ResponseEntity.ok(ApiResponse.success(leaveService.updateBalance(staffId, req.getLeaveBalance())));
    }

    @PostMapping("/{staffId}/entries")
    @Operation(summary = "Add a new leave entry (status defaults to PENDING)")
    public ResponseEntity<ApiResponse<LeaveDocumentDto>> addEntry(
            @PathVariable String staffId,
            @Valid @RequestBody CreateLeaveEntryRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(leaveService.addLeaveEntry(staffId, req)));
    }

    @PutMapping("/{staffId}/entries/{referenceId}")
    @Operation(summary = "Update a leave entry (partial update)")
    public ResponseEntity<ApiResponse<LeaveDocumentDto>> updateEntry(
            @PathVariable String staffId,
            @PathVariable String referenceId,
            @RequestBody UpdateLeaveEntryRequest req) {
        return ResponseEntity.ok(ApiResponse.success(leaveService.updateLeaveEntry(staffId, referenceId, req)));
    }

    @DeleteMapping("/{staffId}/entries/{referenceId}")
    @Operation(summary = "Delete a leave entry")
    public ResponseEntity<ApiResponse<LeaveDocumentDto>> deleteEntry(
            @PathVariable String staffId,
            @PathVariable String referenceId) {
        return ResponseEntity.ok(ApiResponse.success(leaveService.deleteLeaveEntry(staffId, referenceId)));
    }
}
