package com.smartbadge.adl.staff;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartbadge.adl.shared.ApiResponse;
import com.smartbadge.adl.staff.dto.CreateStaffRequest;
import com.smartbadge.adl.staff.dto.UpdateStaffRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
@Tag(name = "Staff Management", description = "CRUD operations for staff profiles")
public class StaffController {

    private final StaffService staffService;

    @GetMapping
    @Operation(summary = "List all staff (paginated)")
    public ResponseEntity<ApiResponse<Page<StaffDto>>> findAll(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(staffService.findAll(pageable)));
    }

    @GetMapping("/{staffId}")
    @Operation(summary = "Get staff by ID — basic profile")
    public ResponseEntity<ApiResponse<StaffDto>> findById(
            @Parameter(description = "Staff identifier, e.g. E4861934") @PathVariable String staffId) {
        return ResponseEntity.ok(ApiResponse.success(staffService.findByStaffId(staffId)));
    }

    @GetMapping("/{staffId}/profile")
    @Operation(summary = "Get full aggregated staff profile (leave + team data)")
    public ResponseEntity<ApiResponse<StaffProfileDto>> getFullProfile(@PathVariable String staffId) {
        return ResponseEntity.ok(ApiResponse.success(staffService.getFullProfile(staffId)));
    }

    @PostMapping
    @Operation(summary = "Create a new staff member")
    public ResponseEntity<ApiResponse<StaffDto>> create(@Valid @RequestBody CreateStaffRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(staffService.create(request)));
    }

    @PutMapping("/{staffId}")
    @Operation(summary = "Update staff information (partial update — only non-null fields are applied)")
    public ResponseEntity<ApiResponse<StaffDto>> update(
            @PathVariable String staffId,
            @Valid @RequestBody UpdateStaffRequest request) {
        return ResponseEntity.ok(ApiResponse.success(staffService.update(staffId, request)));
    }

    @DeleteMapping("/{staffId}")
    @Operation(summary = "Delete a staff member")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String staffId) {
        staffService.delete(staffId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    // ── Address sub-resource ─────────────────────────────────────────────────

    @PostMapping("/{staffId}/addresses")
    @Operation(summary = "Add / replace an address (upserts by addressType)")
    public ResponseEntity<ApiResponse<StaffDto>> addAddress(
            @PathVariable String staffId,
            @Valid @RequestBody AddressDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(staffService.addAddress(staffId, dto)));
    }

    @PutMapping("/{staffId}/addresses/{addressType}")
    @Operation(summary = "Update a specific address type")
    public ResponseEntity<ApiResponse<StaffDto>> updateAddress(
            @PathVariable String staffId,
            @PathVariable AddressType addressType,
            @Valid @RequestBody AddressDto dto) {
        return ResponseEntity.ok(ApiResponse.success(staffService.updateAddress(staffId, addressType, dto)));
    }

    @DeleteMapping("/{staffId}/addresses/{addressType}")
    @Operation(summary = "Remove a specific address type")
    public ResponseEntity<ApiResponse<StaffDto>> removeAddress(
            @PathVariable String staffId,
            @PathVariable AddressType addressType) {
        return ResponseEntity.ok(ApiResponse.success(staffService.removeAddress(staffId, addressType)));
    }
}
