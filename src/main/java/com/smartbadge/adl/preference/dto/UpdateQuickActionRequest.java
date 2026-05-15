package com.smartbadge.adl.preference.dto;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdateQuickActionRequest {
    private String title, subTitle, displayOrder;
}
