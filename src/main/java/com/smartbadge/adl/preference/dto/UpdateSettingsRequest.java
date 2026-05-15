package com.smartbadge.adl.preference.dto;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdateSettingsRequest {
    private String currency, language, timezone, dateFormat;
}
