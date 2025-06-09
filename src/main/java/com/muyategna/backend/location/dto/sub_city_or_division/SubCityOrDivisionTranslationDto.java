package com.muyategna.backend.location.dto.sub_city_or_division;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCityOrDivisionTranslationDto {
    private Long id;
    private Long subCityOrDivisionId;
    private Long languageId;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
