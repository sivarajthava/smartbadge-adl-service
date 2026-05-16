package com.smartbadge.adl.preference;

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

import com.smartbadge.adl.preference.dto.AddQuickActionRequest;
import com.smartbadge.adl.preference.dto.AddWidgetRequest;
import com.smartbadge.adl.preference.dto.CreatePreferenceRequest;
import com.smartbadge.adl.preference.dto.UpdatePreferenceRequest;
import com.smartbadge.adl.preference.dto.UpdateQuickActionRequest;
import com.smartbadge.adl.preference.dto.UpdateSettingsRequest;
import com.smartbadge.adl.preference.dto.UpdateWidgetRequest;
import com.smartbadge.adl.shared.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for user preferences. Path: /api/v1/{userId}/preferences
 *
 * Spec endpoint: GET /api/v1/{userId}/preferences Response schema:
 * UserPreferenceResponseDto (success, userId, userName, widgets, quickActions,
 * preferences, facetData, facetScore)
 */
@RestController
@RequestMapping("/api/v1/{userId}/preferences")
@RequiredArgsConstructor
@Tag(name = "User Preferences", description = "Manage user preferences, widgets and quick actions. "
		+ "GET /api/v1/{userId}/preferences matches the Swagger spec exactly.")
@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request, validation error, or missing parameter", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Requested user preference resource was not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Duplicate resource conflict", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Unable to process your request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
})
public class UserPreferenceController {

	private final UserPreferenceService preferenceService;

	// ── Spec endpoint (GET) ───────────────────────────────────────────────────

	@GetMapping
	@Operation(summary = "Get user preferences", description = "Fetch user preferences along with getting started widgets. "
			+ "Response includes success, userId, userName, widgets, quickActions, "
			+ "preferences (currency/language/timezone/dateFormat), "
			+ "facetData (createdAt/updatedAt) and facetScore.", parameters = @Parameter(name = "userId", description = "Unique user identifier", example = "345351", required = true))
	public ResponseEntity<ApiResponse<UserPreferenceResponseDto>> get(@PathVariable String userId) {
		return ResponseEntity.ok(ApiResponse.success(preferenceService.getByUserId(userId)));
	}

	// ── CRUD ──────────────────────────────────────────────────────────────────

	@PostMapping
	@Operation(summary = "Create user preferences for a given userId")
	public ResponseEntity<ApiResponse<UserPreferenceResponseDto>> create(@PathVariable String userId,
			@Valid @RequestBody CreatePreferenceRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success(preferenceService.create(userId, req)));
	}

	@PutMapping
	@Operation(summary = "Full update of user preferences (replaces widgets/quickActions if provided)")
	public ResponseEntity<ApiResponse<UserPreferenceResponseDto>> update(@PathVariable String userId,
			@RequestBody UpdatePreferenceRequest req) {
		return ResponseEntity.ok(ApiResponse.success(preferenceService.update(userId, req)));
	}

	@PatchMapping("/settings")
	@Operation(summary = "Partial update of locale/display settings only", description = "Updates one or more of: currency, language, timezone, dateFormat")
	public ResponseEntity<ApiResponse<UserPreferenceResponseDto>> updateSettings(@PathVariable String userId,
			@RequestBody UpdateSettingsRequest req) {
		return ResponseEntity.ok(ApiResponse.success(preferenceService.updateSettings(userId, req)));
	}

	@DeleteMapping
	@Operation(summary = "Delete all preferences for a user")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String userId) {
		preferenceService.delete(userId);
		return ResponseEntity.ok(ApiResponse.success());
	}

	// ── Widget sub-resource ───────────────────────────────────────────────────

	@PostMapping("/widgets")
	@Operation(summary = "Add a widget (auto-creates preference doc if absent)")
	public ResponseEntity<ApiResponse<UserPreferenceResponseDto>> addWidget(@PathVariable String userId,
			@Valid @RequestBody AddWidgetRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success(preferenceService.addWidget(userId, req)));
	}

	@PutMapping("/widgets/{widgetId}")
	@Operation(summary = "Update a specific widget by widgetId")
	public ResponseEntity<ApiResponse<UserPreferenceResponseDto>> updateWidget(@PathVariable String userId,
			@PathVariable String widgetId, @RequestBody UpdateWidgetRequest req) {
		return ResponseEntity.ok(ApiResponse.success(preferenceService.updateWidget(userId, widgetId, req)));
	}

	@DeleteMapping("/widgets/{widgetId}")
	@Operation(summary = "Remove a widget by widgetId")
	public ResponseEntity<ApiResponse<UserPreferenceResponseDto>> removeWidget(@PathVariable String userId,
			@PathVariable String widgetId) {
		return ResponseEntity.ok(ApiResponse.success(preferenceService.removeWidget(userId, widgetId)));
	}

	// ── Quick Action sub-resource ─────────────────────────────────────────────

	@PostMapping("/quick-actions")
	@Operation(summary = "Add a quick action (auto-creates preference doc if absent)")
	public ResponseEntity<ApiResponse<UserPreferenceResponseDto>> addQuickAction(@PathVariable String userId,
			@Valid @RequestBody AddQuickActionRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success(preferenceService.addQuickAction(userId, req)));
	}

	@PutMapping("/quick-actions/{actionId}")
	@Operation(summary = "Update a specific quick action by actionId")
	public ResponseEntity<ApiResponse<UserPreferenceResponseDto>> updateQuickAction(@PathVariable String userId,
			@PathVariable String actionId, @RequestBody UpdateQuickActionRequest req) {
		return ResponseEntity.ok(ApiResponse.success(preferenceService.updateQuickAction(userId, actionId, req)));
	}

	@DeleteMapping("/quick-actions/{actionId}")
	@Operation(summary = "Remove a quick action by actionId")
	public ResponseEntity<ApiResponse<UserPreferenceResponseDto>> removeQuickAction(@PathVariable String userId,
			@PathVariable String actionId) {
		return ResponseEntity.ok(ApiResponse.success(preferenceService.removeQuickAction(userId, actionId)));
	}
}
