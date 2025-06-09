package com.muyategna.backend.location.dto.country;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CountryCreateDto {

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
