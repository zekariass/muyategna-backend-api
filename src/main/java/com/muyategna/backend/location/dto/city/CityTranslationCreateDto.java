package com.muyategna.backend.location.dto.city;

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
public class CityTranslationCreateDto {
    @NotNull(message = "City is required")
    private Long cityId;

    @NotNull(message = "Language is required")
    private Long languageId;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;
}
