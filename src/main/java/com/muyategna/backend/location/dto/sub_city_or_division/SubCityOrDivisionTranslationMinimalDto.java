package com.muyategna.backend.location.dto.sub_city_or_division;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCityOrDivisionTranslationMinimalDto {
    private Long id;
    private Long subCityOrDivisionId;
    private Long languageId;
    private String name;
}
