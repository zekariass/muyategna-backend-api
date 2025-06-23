package com.muyategna.backend.location.dto.country;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CountryDto {
    private Long id;
    private String name;
    private String countryIsoCode2;
    private String countryIsoCode3;
    private String countryIsoCodeNumeric;
    private String continent;
    private String description;
    private boolean isGlobal;
    private String currency;
    private String taxpayerIdType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
