package com.smartbadge.adl.preference.dto;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdateWidgetRequest {
    private String actionId, title, subTitle, displayOrder;
}
