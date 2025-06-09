package com.muyategna.backend.location.dto.country;

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
public class CountryTranslationUpdateDto {

    @NotNull(message = "ID is required")
    private Long id;

    @NotNull(message = "Country is required")
    private Long countryId;

    private Long languageId;

    @NotBlank(message = "Name is required")
    private String name;

    private String continent;
    private String description;

}
