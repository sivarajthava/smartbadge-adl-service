package com.smartbadge.adl.preference.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AddQuickActionRequest {
    @NotBlank(message = "Action ID is required") private String actionId;
    @NotBlank(message = "Title is required")    private String title;
    private String subTitle;
    @NotBlank(message = "Display order is required") private String displayOrder;
}
