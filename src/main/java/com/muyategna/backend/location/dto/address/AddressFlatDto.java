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
public class AddressFlatDto {
    private Long id;
    private String countryName;
    private String regionName;
    private String cityName;
    private String subCityOrDivisionName;

    private String locality;
    private String street;
    private String landmark;
    private String postalCode;

    private Double latitude;
    private Double longitude;
    private String fullAddress;
}
