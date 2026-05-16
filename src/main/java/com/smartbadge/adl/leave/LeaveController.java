package com.smartbadge.adl.leave;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartbadge.adl.leave.dto.CreateLeaveEntryRequest;
import com.smartbadge.adl.leave.dto.UpdateLeaveBalanceRequest;
import com.smartbadge.adl.leave.dto.UpdateLeaveEntryRequest;
import com.smartbadge.adl.shared.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/leave")
@RequiredArgsConstructor
@Tag(name = "Leave Management", description = "Manage staff leave documents and entries")
@ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request, validation error, or missing parameter", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Requested leave resource was not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Duplicate resource conflict", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Unable to process your request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
})
public class LeaveController {

    private final LeaveService leaveService;

    @GetMapping("/admin/{staffId}")
    @Operation(summary = "Get leave document for a staff member")
    public ResponseEntity<ApiResponse<LeaveDocumentDto>> get(@PathVariable String staffId) {
        return ResponseEntity.ok(ApiResponse.success(leaveService.getByStaffId(staffId)));
    }

    @PostMapping("/admin/{staffId}")
    @Operation(summary = "Initialize leave document (idempotent)")
    public ResponseEntity<ApiResponse<LeaveDocumentDto>> initialize(
            @PathVariable String staffId,
            @Valid @RequestBody UpdateLeaveBalanceRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(leaveService.initializeOrUpdate(staffId, req.getLeaveBalance())));
    }

    @PatchMapping("/admin/{staffId}/balance")
    @Operation(summary = "Update leave balance")
    public ResponseEntity<ApiResponse<LeaveDocumentDto>> updateBalance(
            @PathVariable String staffId,
            @Valid @RequestBody UpdateLeaveBalanceRequest req) {
        return ResponseEntity.ok(ApiResponse.success(leaveService.updateBalance(staffId, req.getLeaveBalance())));
    }

    @PostMapping("/{staffId}/apply")
    @Operation(summary = "Add a new leave entry (status defaults to PENDING)")
    public ResponseEntity<ApiResponse<LeaveDocumentDto>> addEntry(
            @PathVariable String staffId,
            @Valid @RequestBody CreateLeaveEntryRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(leaveService.addLeaveEntry(staffId, req)));
    }

    @PutMapping("/{staffId}/approve/{referenceId}")
    @Operation(summary = "Update a leave entry (partial update)")
    public ResponseEntity<ApiResponse<LeaveDocumentDto>> updateEntry(
            @PathVariable String staffId,
            @PathVariable String referenceId,
            @RequestBody UpdateLeaveEntryRequest req) {
        return ResponseEntity.ok(ApiResponse.success(leaveService.updateLeaveEntry(staffId, referenceId, req)));
    }

    @DeleteMapping("/{staffId}/delete/{referenceId}")
    @Operation(summary = "Delete a leave entry")
    public ResponseEntity<ApiResponse<LeaveDocumentDto>> deleteEntry(
            @PathVariable String staffId,
            @PathVariable String referenceId) {
        return ResponseEntity.ok(ApiResponse.success(leaveService.deleteLeaveEntry(staffId, referenceId)));
    }
}
