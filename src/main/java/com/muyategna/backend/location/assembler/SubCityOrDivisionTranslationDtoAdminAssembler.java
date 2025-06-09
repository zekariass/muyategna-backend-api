package com.muyategna.backend.location.assembler;

import com.muyategna.backend.location.controller.admin.SubCityOrDivisionTranslationAdminController;
import com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionTranslationDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SubCityOrDivisionTranslationDtoAdminAssembler {

    public EntityModel<com.muyategna.backend.location.dto.sub_city_or_division.SubCityOrDivisionTranslationDto> toModel(SubCityOrDivisionTranslationDto subCityOrDivisionTranslationDto, int page, int size, String sortBy, HttpServletRequest request) {
        return EntityModel.of(
                subCityOrDivisionTranslationDto,
                linkTo(methodOn(SubCityOrDivisionTranslationAdminController.class).getSubCityOrDivisionTranslationById(subCityOrDivisionTranslationDto.getId(), request)).withSelfRel(),
                linkTo(methodOn(SubCityOrDivisionTranslationAdminController.class).getSubCityOrDivisionTranslationsBySubCityOrDivisionId(subCityOrDivisionTranslationDto.getSubCityOrDivisionId(), page, size, sortBy, request)).withRel("allSubCityOrDivisionTranslations")
        );
    }

    public EntityModel<SubCityOrDivisionTranslationDto> toModel(SubCityOrDivisionTranslationDto subCityOrDivisionTranslationDto, HttpServletRequest request) {
        return EntityModel.of(
                subCityOrDivisionTranslationDto,
                linkTo(methodOn(SubCityOrDivisionTranslationAdminController.class).getSubCityOrDivisionTranslationById(subCityOrDivisionTranslationDto.getId(), request)).withSelfRel(),
                linkTo(methodOn(SubCityOrDivisionTranslationAdminController.class).getSubCityOrDivisionTranslationsBySubCityOrDivisionId(subCityOrDivisionTranslationDto.getSubCityOrDivisionId(), 1, 10, "id", request)).withRel("allSubCityOrDivisionTranslations")
        );
    }

    public CollectionModel<EntityModel<SubCityOrDivisionTranslationDto>> toCollectionModel(Iterable<? extends SubCityOrDivisionTranslationDto> subCityOrDivisionTranslationDtoList, int page, int size, String sortBy, HttpServletRequest request) {
        List<EntityModel<SubCityOrDivisionTranslationDto>> subCityOrDivisionTranslationsModel = StreamSupport.stream(subCityOrDivisionTranslationDtoList.spliterator(), false)
                .map(subCityOrDivisionTranslationDto -> this.toModel(subCityOrDivisionTranslationDto, request))
                .toList();
        return CollectionModel.of(subCityOrDivisionTranslationsModel,
                linkTo(methodOn(SubCityOrDivisionTranslationAdminController.class).getSubCityOrDivisionTranslationById(subCityOrDivisionTranslationDtoList.iterator().next().getId(), request)).withSelfRel(),
                linkTo(methodOn(SubCityOrDivisionTranslationAdminController.class).getSubCityOrDivisionTranslationsBySubCityOrDivisionId(subCityOrDivisionTranslationDtoList.iterator().next().getSubCityOrDivisionId(), page, size, sortBy, request)).withRel("allSubCityOrDivisionTranslations")
        );
    }
}
