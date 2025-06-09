package com.muyategna.backend.location.dto.city;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityTranslationUpdateDto {

    @NotNull(message = "ID is required")
    private Long id;

    @NotNull(message = "City is required")
    private Long cityId;

    @NotNull(message = "Language is required")
    private Long languageId;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;
}
