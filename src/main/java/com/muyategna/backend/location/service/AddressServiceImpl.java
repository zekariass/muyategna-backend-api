package com.muyategna.backend.location.service;

import com.muyategna.backend.location.dto.address.AddressDto;
import com.muyategna.backend.location.dto.address.AddressUpdateDto;
import com.muyategna.backend.location.entity.*;
import com.muyategna.backend.location.mapper.AddressMapper;
import com.muyategna.backend.location.repository.*;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CountryRepository countryRepository;
    private final RegionRepository regionRepository;
    private final CityRepository cityRepository;
    private final SubCityOrDivisionRepository subCityOrDivisionRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, CountryRepository countryRepository, RegionRepository regionRepository, CityRepository cityRepository, SubCityOrDivisionRepository subCityOrDivisionRepository) {
        this.addressRepository = addressRepository;
        this.countryRepository = countryRepository;
        this.regionRepository = regionRepository;
        this.cityRepository = cityRepository;
        this.subCityOrDivisionRepository = subCityOrDivisionRepository;
    }

    @Override
    public AddressDto updateAddress(Long addressId, AddressUpdateDto addressUpdateDto) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));
        Country country = countryRepository.findById(addressUpdateDto.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + addressUpdateDto.getCountryId()));
        Region region = regionRepository.findById(addressUpdateDto.getRegionId())
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with id: " + addressUpdateDto.getRegionId()));
        City city = cityRepository.findById(addressUpdateDto.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + addressUpdateDto.getCityId()));
        SubCityOrDivision subCityOrDivision = subCityOrDivisionRepository.findById(addressUpdateDto.getSubCityOrDivisionId())
                .orElseThrow(() -> new ResourceNotFoundException("Sub city or division not found with id: " + addressUpdateDto.getSubCityOrDivisionId()));

        Address populatedAddress = AddressMapper.toEntity(addressUpdateDto, address, country, region, city, subCityOrDivision);
        Address updatedAddress = addressRepository.save(populatedAddress);

        return AddressMapper.toDto(updatedAddress);
    }
}
