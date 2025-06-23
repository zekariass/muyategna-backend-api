package com.muyategna.backend.location.mapper;

import com.muyategna.backend.location.dto.address.AddressCreateDto;
import com.muyategna.backend.location.dto.address.AddressDto;
import com.muyategna.backend.location.dto.address.AddressUpdateDto;
import com.muyategna.backend.location.entity.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 * Mapper class for converting Address entities to Address DTOs.
 * This class provides a method to convert an Address entity to its corresponding DTO representation.
 */
public final class AddressMapper {


    /**
     * Converts an Address entity to an Address DTO.
     *
     * @param address the Address entity to convert
     * @return the converted Address DTO, or null if the input address is null
     */
    public static AddressDto toDto(Address address) {
        if (address == null) {
            return null;
        }
        return AddressDto.builder()
                .id(address.getId())
                .countryMinimalDto(CountryMapper.toMinimalDto(address.getCountry()))
                .regionMinimalDto(RegionMapper.toMinimalDto(address.getRegion()))
                .cityMinimalDto(CityMapper.toMinimalDto(address.getCity()))
                .subCityOrDivisionMinimalDto(SubCityOrDivisionMapper.toMinimalDto(address.getSubCityOrDivision()))
                .locality(address.getLocality())
                .street(address.getStreet())
                .landmark(address.getLandmark())
                .postalCode(address.getPostalCode())
                .latitude(address.getGeoPoint().getY())
                .longitude(address.getGeoPoint().getX())
                .fullAddress(address.getFullAddress())
                .createdAt(address.getCreatedAt())
                .build();
    }


    public static Address toEntity(AddressUpdateDto addressDto,
                                   Address address,
                                   Country country,
                                   Region region,
                                   City city,
                                   SubCityOrDivision subCityOrDivision) {
        if (addressDto == null) {
            return null;
        }
        address.setId(addressDto.getId());
        address.setCountry(country);
        address.setRegion(region);
        address.setCity(city);
        address.setSubCityOrDivision(subCityOrDivision);
        address.setLocality(addressDto.getLocality());
        address.setStreet(addressDto.getStreet());
        address.setLandmark(addressDto.getLandmark());
        address.setPostalCode(addressDto.getPostalCode());
        address.setGeoPoint(createPoint(addressDto.getLatitude(), addressDto.getLongitude()));

        return address;
    }

    public static Address toEntity(AddressCreateDto addressDto,
                                   Country country,
                                   Region region,
                                   City city,
                                   SubCityOrDivision subCityOrDivision) {
        if (addressDto == null) {
            return null;
        }

        Address address = new Address();

        address.setCountry(country);
        address.setRegion(region);
        address.setCity(city);
        address.setSubCityOrDivision(subCityOrDivision);
        address.setLocality(addressDto.getLocality());
        address.setStreet(addressDto.getStreet());
        address.setLandmark(addressDto.getLandmark());
        address.setPostalCode(addressDto.getPostalCode());
        address.setGeoPoint(createPoint(addressDto.getLatitude(), addressDto.getLongitude()));

        address.setFullAddress(createFullAddress(address));
        return address;
    }

    public static Address toEntity(AddressDto addressDto,
                                   Country country,
                                   Region region,
                                   City city,
                                   SubCityOrDivision subCityOrDivision) {
        if (addressDto == null) {
            return null;
        }
        Address address = new com.muyategna.backend.location.entity.Address();
        address.setId(addressDto.getId());
        address.setCountry(country);
        address.setRegion(region);
        address.setCity(city);
        address.setSubCityOrDivision(subCityOrDivision);
        address.setLocality(addressDto.getLocality());
        address.setStreet(addressDto.getStreet());
        address.setLandmark(addressDto.getLandmark());
        address.setPostalCode(addressDto.getPostalCode());
        address.setGeoPoint(createPoint(addressDto.getLatitude(), addressDto.getLongitude()));
        address.setFullAddress(addressDto.getFullAddress());
        address.setCreatedAt(addressDto.getCreatedAt());

        return address;
    }

    private static Point createPoint(Double latitude, Double longitude) {
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }


    private static String createFullAddress(Address address) {
        String fullAddress = "";
        if (address.getLocality() != null) {
            fullAddress += address.getLocality() + ", ";
        }
        if (address.getStreet() != null) {
            fullAddress += address.getStreet() + ", ";
        }
        if (address.getLandmark() != null) {
            fullAddress += address.getLandmark() + ", ";
        }
        if (address.getPostalCode() != null) {
            fullAddress += address.getPostalCode();
        }
        if (address.getSubCityOrDivision() != null) {
            fullAddress += ", " + address.getSubCityOrDivision().getName();
        }
        if (address.getCity() != null) {
            fullAddress += ", " + address.getCity().getName();
        }
        if (address.getRegion() != null) {
            fullAddress += ", " + address.getRegion().getName();
        }
        if (address.getCountry() != null) {
            fullAddress += ", " + address.getCountry().getName();
        }
        return fullAddress;
    }
}
