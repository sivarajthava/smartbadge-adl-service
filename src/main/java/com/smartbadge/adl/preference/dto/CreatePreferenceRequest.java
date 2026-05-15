package com.smartbadge.adl.preference.dto;
import com.smartbadge.adl.preference.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreatePreferenceRequest {
    @NotBlank(message = "User name is required") private String userName;
    private List<Widget>      widgets;
    private List<QuickAction> quickActions;
    private UserPreferences   preferences;
    private String            facetScore;
}
