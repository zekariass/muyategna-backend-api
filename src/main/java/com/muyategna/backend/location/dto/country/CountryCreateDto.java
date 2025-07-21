package com.muyategna.backend.location.dto.country;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    private String currency;

    private String taxpayerIdType;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

}
