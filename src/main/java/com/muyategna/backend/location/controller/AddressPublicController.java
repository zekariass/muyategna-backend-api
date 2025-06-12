package com.muyategna.backend.location.controller;

import com.muyategna.backend.common.ApiResponse;
import com.muyategna.backend.location.assembler.AddressDtoModelAdminAssembler;
import com.muyategna.backend.location.dto.address.AddressDto;
import com.muyategna.backend.location.dto.address.AddressUpdateDto;
import com.muyategna.backend.location.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/public/location/address")
@Tag(name = "Address Public Controller", description = "Address Public API")
public class AddressPublicController {

    private final AddressService addressService;
    private final AddressDtoModelAdminAssembler addressDtoModelAdminAssembler;

    @Autowired
    public AddressPublicController(AddressService addressService,
                                   AddressDtoModelAdminAssembler addressDtoModelAdminAssembler) {
        this.addressService = addressService;
        this.addressDtoModelAdminAssembler = addressDtoModelAdminAssembler;
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Update address", description = "Update address by id. This operation is only for admin")
    public ResponseEntity<ApiResponse<EntityModel<AddressDto>>> updateAddress(@Parameter(description = "Id of the address to be updated", required = true) @PathVariable Long addressId,
                                                                              @Parameter(description = "Address object to be updated", required = true) @Valid @RequestBody AddressUpdateDto addressUpdateDto,
                                                                              HttpServletRequest request) {
        AddressDto updatedAddress = addressService.updateAddress(addressId, addressUpdateDto);
        EntityModel<AddressDto> entityModel = addressDtoModelAdminAssembler.toModel(updatedAddress, request);
        ApiResponse<EntityModel<AddressDto>> response = ApiResponse.<EntityModel<AddressDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Cities retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return ResponseEntity.ok(response);
    }


}
