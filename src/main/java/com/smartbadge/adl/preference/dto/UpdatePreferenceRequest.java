package com.smartbadge.adl.preference.dto;
import com.smartbadge.adl.preference.*;
import lombok.*;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdatePreferenceRequest {
    private String userName, facetScore;
    private List<Widget>      widgets;
    private List<QuickAction> quickActions;
    private UserPreferences   preferences;
}
