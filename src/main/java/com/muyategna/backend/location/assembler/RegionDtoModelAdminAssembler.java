package com.muyategna.backend.location.assembler;

import com.muyategna.backend.location.controller.admin.CountryAdminController;
import com.muyategna.backend.location.controller.admin.RegionAdminController;
import com.muyategna.backend.location.dto.region.RegionDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RegionDtoModelAdminAssembler {

    public EntityModel<RegionDto> toModel(RegionDto regionDto, int page, int size, String sortBy, HttpServletRequest request) {
        return EntityModel.of(regionDto,
                linkTo(methodOn(RegionAdminController.class).getRegionById(regionDto.getId(), request)).withSelfRel(),
                linkTo(methodOn(RegionAdminController.class).getAllRegions(request)).withRel("allRegions"),
                linkTo(methodOn(CountryAdminController.class).getCountryById(regionDto.getCountryId(), request)).withRel("country")
        );
    }

    public EntityModel<RegionDto> toModel(RegionDto regionDto, HttpServletRequest request) {
        return EntityModel.of(regionDto,
                linkTo(methodOn(RegionAdminController.class).getRegionById(regionDto.getId(), request)).withSelfRel(),
                linkTo(methodOn(RegionAdminController.class).getAllRegions(request)).withRel("allRegions"),
                linkTo(methodOn(CountryAdminController.class).getCountryById(regionDto.getCountryId(), request)).withRel("country")
        );
    }
}
