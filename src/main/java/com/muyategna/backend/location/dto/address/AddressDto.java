package com.muyategna.backend.location.dto.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AddressDto is a Data Transfer Object (DTO) that represents an address in the system.
 * It contains fields for various address components such as country, region, city,
 * sub-city or division, locality, street, landmark, postal code, geographical point,
 * full address string, and creation timestamp.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {
    private Long id;
    private Long countryId;
    private Long regionId;
    private Long cityId;
    private Long subCityOrDivisionId;

    private String locality;
    private String street;
    private String landmark;
    private String postalCode;

    private Double latitude;
    private Double longitude;
    private String fullAddress;
    private LocalDateTime createdAt;
}
