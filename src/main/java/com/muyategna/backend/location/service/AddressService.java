package com.muyategna.backend.location.service;

import com.muyategna.backend.location.dto.address.AddressCreateDto;
import com.muyategna.backend.location.dto.address.AddressDto;
import com.muyategna.backend.location.dto.address.AddressUpdateDto;
import com.muyategna.backend.location.entity.Address;

public interface AddressService {
    Address createAddress(AddressCreateDto addressCreateDto);

    AddressDto updateAddress(Long addressId, AddressUpdateDto addressUpdateDto);
}
