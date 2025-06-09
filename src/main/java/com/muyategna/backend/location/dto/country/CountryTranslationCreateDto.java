package com.muyategna.backend.location.dto.country;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryTranslationCreateDto {
    @NotNull(message = "Country is required")
    private Long countryId;

    @NotNull(message = "Language is required")
    private Long languageId;

    @NotNull(message = "Name is required")
    private String name;
    private String continent;
    private String description;
}
