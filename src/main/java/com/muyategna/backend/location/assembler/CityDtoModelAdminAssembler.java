package com.muyategna.backend.location.assembler;

import com.muyategna.backend.location.controller.admin.CityAdminController;
import com.muyategna.backend.location.controller.admin.RegionAdminController;
import com.muyategna.backend.location.dto.city.CityDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CityDtoModelAdminAssembler {
    public EntityModel<CityDto> toModel(CityDto cityDto, int page, int size, String sortBy, HttpServletRequest request) {
        return EntityModel.of(cityDto,
                linkTo(methodOn(CityAdminController.class).getCityById(cityDto.getId(), request)).withSelfRel(),
                linkTo(methodOn(CityAdminController.class).getAllCities(page, size, sortBy, request)).withRel("allCities"),
                linkTo(methodOn(RegionAdminController.class).getRegionById(cityDto.getRegionId(), request)).withRel("region")
        );
    }
}
