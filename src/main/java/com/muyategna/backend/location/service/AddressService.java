package com.muyategna.backend.location.service;

import com.muyategna.backend.location.dto.address.AddressDto;
import com.muyategna.backend.location.dto.address.AddressUpdateDto;

public interface AddressService {
    AddressDto updateAddress(Long addressId, AddressUpdateDto addressUpdateDto);
}
