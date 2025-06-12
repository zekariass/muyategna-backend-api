package com.muyategna.backend.location.assembler;

import com.muyategna.backend.location.dto.address.AddressDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

@Component
public class AddressDtoModelAdminAssembler {

    public EntityModel<AddressDto> toModel(AddressDto addressDto, HttpServletRequest request) {
        return EntityModel.of(addressDto);
    }
}
