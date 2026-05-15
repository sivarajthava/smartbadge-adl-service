package com.smartbadge.adl.preference;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Top-level response DTO — field names match the Swagger spec exactly. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPreferenceResponseDto {
	/** Always true on success — matches spec field */
	private Boolean success;
	private String userId, userName, facetScore;
	private List<Widget> widgets;
	private List<QuickAction> quickActions;
	private UserPreferences preferences;
	private FacetDataDto facetData;
}
