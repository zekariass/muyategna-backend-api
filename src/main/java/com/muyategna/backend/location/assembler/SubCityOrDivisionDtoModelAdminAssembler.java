package com.muyategna.backend.location.assembler;

import com.muyategna.backend.location.controller.admin.SubCityOrDivisionAdminController;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SubCityOrDivisionDtoModelAdminAssembler {

    public EntityModel<SubCityOrDivisionDto> toModel(SubCityOrDivisionDto subCityOrDivisionDto, HttpServletRequest request) {
        return EntityModel.of(subCityOrDivisionDto,
                linkTo(methodOn(SubCityOrDivisionAdminController.class).getSubCityOrDivisionById(subCityOrDivisionDto.getId(), request)).withSelfRel(),
                linkTo(methodOn(SubCityOrDivisionAdminController.class).getAllSubCityOrDivisions(1, 10, "id", request)).withRel("allSubCityOrDivisions"),
                linkTo(methodOn(SubCityOrDivisionAdminController.class).getSubCityOrDivisionByCityId(subCityOrDivisionDto.getCityId(), 1, 10, "id", request)).withRel("city")
        );
    }

    public EntityModel<SubCityOrDivisionDto> toModel(SubCityOrDivisionDto subCityOrDivisionDto, int page, int size, String sortBy, HttpServletRequest request) {
        return EntityModel.of(subCityOrDivisionDto,
                linkTo(methodOn(SubCityOrDivisionAdminController.class).getSubCityOrDivisionById(subCityOrDivisionDto.getId(), request)).withSelfRel(),
                linkTo(methodOn(SubCityOrDivisionAdminController.class).getAllSubCityOrDivisions(page, size, sortBy, request)).withRel("allSubCityOrDivisions"),
                linkTo(methodOn(SubCityOrDivisionAdminController.class).getSubCityOrDivisionByCityId(subCityOrDivisionDto.getCityId(), page, size, sortBy, request)).withRel("city")
        );
    }


    public CollectionModel<EntityModel<SubCityOrDivisionDto>> toCollectionModel(Iterable<? extends SubCityOrDivisionDto> subCityOrDivisionsDto, int page, int size, String sortBy, HttpServletRequest request) {
        List<EntityModel<SubCityOrDivisionDto>> subCityOrDivisionsModel = StreamSupport.stream(subCityOrDivisionsDto.spliterator(), false)
                .map(subCityOrDivisionDto -> this.toModel(subCityOrDivisionDto, page, size, sortBy, request))
                .toList();
        return CollectionModel.of(subCityOrDivisionsModel,
                linkTo(methodOn(SubCityOrDivisionAdminController.class).getAllSubCityOrDivisions(page, size, sortBy, request)).withSelfRel()
        );
    }
}
