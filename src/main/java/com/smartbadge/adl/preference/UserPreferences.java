package com.smartbadge.adl.preference;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPreferences {
    @Builder.Default private String currency  = "USD";
    @Builder.Default private String language  = "en";
    @Builder.Default private String timezone  = "Asia/Dubai";
    @Builder.Default private String dateFormat = "dd-MM-yyyy";
}
