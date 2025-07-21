package com.muyategna.backend.location.mapper;

import com.muyategna.backend.location.dto.address.AddressCreateDto;
import com.muyategna.backend.location.dto.address.AddressDto;
import com.muyategna.backend.location.dto.address.AddressFlatDto;
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


    public static AddressFlatDto toFlatDto(Address address) {
        if (address == null) {
            return null;
        }

        String countryName = "";
        String regionName = "";
        String cityName = "";
        String subCityOrDivisionName = "";

        if (address.getSubCityOrDivision() != null) {
            subCityOrDivisionName = address.getSubCityOrDivision().getName();
        }

        if (address.getCity() != null) {
            cityName = address.getCity().getName();
        }

        if (address.getRegion() != null) {
            regionName = address.getRegion().getName();
        }

        if (address.getCountry() != null) {
            countryName = address.getCountry().getName();
        }

        return AddressFlatDto.builder()
                .id(address.getId())
                .countryName(countryName)
                .regionName(regionName)
                .cityName(cityName)
                .subCityOrDivisionName(subCityOrDivisionName)
                .locality(address.getLocality())
                .street(address.getStreet())
                .landmark(address.getLandmark())
                .postalCode(address.getPostalCode())
                .latitude(address.getGeoPoint().getY())
                .longitude(address.getGeoPoint().getX())
                .fullAddress(address.getFullAddress())
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

        if (addressDto.getLatitude() != null && addressDto.getLongitude() != null) {
            address.setGeoPoint(createPoint(addressDto.getLatitude(), addressDto.getLongitude()));
        }else if (subCityOrDivision != null && subCityOrDivision.getGeoPoint() != null) {
            address.setGeoPoint(subCityOrDivision.getGeoPoint());
        }else if (city != null && city.getGeoPoint() != null) {
            address.setGeoPoint(city.getGeoPoint());
        } else if (region != null && region.getGeoPoint() != null) {
            address.setGeoPoint(region.getGeoPoint());
        } else if (country != null && country.getGeoPoint() != null) {
            address.setGeoPoint(country.getGeoPoint());
        }

        address.setFullAddress(createFullAddress(address));
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
        address.setIsDefaultGeoPoint(true);

        if (addressDto.getLatitude() != null && addressDto.getLongitude() != null) {
            address.setGeoPoint(createPoint(addressDto.getLatitude(), addressDto.getLongitude()));
            address.setIsDefaultGeoPoint(false);
        }else if (subCityOrDivision != null && subCityOrDivision.getGeoPoint() != null) {
            address.setGeoPoint(subCityOrDivision.getGeoPoint());
        }else if (city != null && city.getGeoPoint() != null) {
            address.setGeoPoint(city.getGeoPoint());
        } else if (region != null && region.getGeoPoint() != null) {
            address.setGeoPoint(region.getGeoPoint());
        } else if (country != null && country.getGeoPoint() != null) {
            address.setGeoPoint(country.getGeoPoint());
        }

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
