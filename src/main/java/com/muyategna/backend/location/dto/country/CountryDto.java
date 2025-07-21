package com.muyategna.backend.location.dto.country;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
