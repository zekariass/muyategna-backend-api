package com.muyategna.backend.location.dto.country;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CountryLocalizedDto {
    private Long id;
    private String name;
    private String countryIsoCode2;
    private String countryIsoCode3;
    private String countryIsoCodeNumeric;
    private String continent;
    private String description;
    private String currency;
    private String taxpayerIdType;
    private boolean isGlobal;
}
