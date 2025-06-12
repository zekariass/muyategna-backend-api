package com.muyategna.backend.location.dto.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class AddressUpdateDto {
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
}
