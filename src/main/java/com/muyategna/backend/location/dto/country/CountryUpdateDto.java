package com.muyategna.backend.location.dto.country;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CountryUpdateDto {

    @NotNull(message = "ID is required")
    @Min(value = 1, message = "Country ID must be greater than 0")
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Code 2 is required")
    private String countryIsoCode2;

    private String countryIsoCode3;

    private String countryIsoCodeNumeric;

    private String continent;

    private String description;

    private boolean isGlobal;
}
