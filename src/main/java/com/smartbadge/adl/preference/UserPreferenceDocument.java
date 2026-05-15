package com.smartbadge.adl.preference;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_preference")
public class UserPreferenceDocument {
	@Id
	private String id;
	@Indexed(unique = true)
	private String userId;
	private String userName;
	@Builder.Default
	private List<Widget> widgets = new ArrayList<>();
	@Builder.Default
	private List<QuickAction> quickActions = new ArrayList<>();
	@Builder.Default
	private UserPreferences preferences = new UserPreferences();
	private String facetScore;
	@CreatedDate
	private Instant createdAt;
	@LastModifiedDate
	private Instant updatedAt;
}
