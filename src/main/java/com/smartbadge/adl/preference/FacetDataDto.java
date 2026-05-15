package com.smartbadge.adl.preference;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/** facetData block: ISO-8601 timestamp strings (e.g. 2023-09-07T10:09:00.000Z) */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacetDataDto {
    private String createdAt, updatedAt;
}
