package com.smartbadge.adl.preference.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AddWidgetRequest {
    @NotBlank(message = "Widget ID is required")    private String widgetId;
    @NotBlank(message = "Action ID is required")    private String actionId;
    @NotBlank(message = "Title is required")        private String title;
    private String subTitle;
    @NotBlank(message = "Display order is required") private String displayOrder;
}
