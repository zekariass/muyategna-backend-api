package com.muyategna.backend.location.dto.sub_city_or_division;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCityOrDivisionTranslationUpdateDto {
    @NotNull(message = "ID is required")
    private Long id;

    @NotNull(message = "Sub city or division is required")
    private Long subCityOrDivisionId;

    @NotNull(message = "Language is required")
    private Long languageId;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;
}
