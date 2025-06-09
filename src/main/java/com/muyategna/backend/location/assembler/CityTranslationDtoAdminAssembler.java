package com.muyategna.backend.location.assembler;

import com.muyategna.backend.location.controller.admin.CityTranslationAdminController;
import com.muyategna.backend.location.dto.city.CityTranslationDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CityTranslationDtoAdminAssembler {

    public EntityModel<CityTranslationDto> toModel(CityTranslationDto cityTranslationDto, int page, int size, String sortBy, HttpServletRequest request) {
        return EntityModel.of(
                cityTranslationDto,
                linkTo(methodOn(CityTranslationAdminController.class).getCityTranslationsById(cityTranslationDto.getId(), request)).withSelfRel(),
                linkTo(methodOn(CityTranslationAdminController.class).getCityTranslationsByCityId(cityTranslationDto.getCityId(), page, size, sortBy, request)).withRel("allCityTranslations")
        );
    }

    public EntityModel<CityTranslationDto> toModel(CityTranslationDto cityTranslationDto, HttpServletRequest request) {
        return EntityModel.of(
                cityTranslationDto,
                linkTo(methodOn(CityTranslationAdminController.class).getCityTranslationsById(cityTranslationDto.getId(), request)).withSelfRel(),
                linkTo(methodOn(CityTranslationAdminController.class).getCityTranslationsByCityId(cityTranslationDto.getCityId(), 1, 10, "id", request)).withRel("allCityTranslations")
        );
    }

    public CollectionModel<EntityModel<CityTranslationDto>> toCollectionModel(Iterable<? extends CityTranslationDto> cityTranslationDtoList, int page, int size, String sortBy, HttpServletRequest request) {
        List<EntityModel<CityTranslationDto>> cityTranslationsModel = StreamSupport.stream(cityTranslationDtoList.spliterator(), false)
                .map(cityTranslationDto -> this.toModel(cityTranslationDto, request))
                .toList();
        return CollectionModel.of(cityTranslationsModel,
                linkTo(methodOn(CityTranslationAdminController.class).getCityTranslationsById(cityTranslationDtoList.iterator().next().getId(), request)).withSelfRel(),
                linkTo(methodOn(CityTranslationAdminController.class).getCityTranslationsByCityId(cityTranslationDtoList.iterator().next().getCityId(), page, size, sortBy, request)).withRel("allCityTranslations")
        );
    }
}
