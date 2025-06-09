package com.muyategna.backend.location.assembler;

import com.muyategna.backend.location.controller.admin.CountryTranslationAdminController;
import com.muyategna.backend.location.dto.country.CountryTranslationDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CountryTranslationDtoAdminAssembler {

    public EntityModel<CountryTranslationDto> toModel(CountryTranslationDto countryTranslationDto, HttpServletRequest request) {
        return EntityModel.of(
                countryTranslationDto,
                linkTo(methodOn(CountryTranslationAdminController.class).getCountryTranslationById(countryTranslationDto.getId(), request)).withSelfRel(),
                linkTo(methodOn(CountryTranslationAdminController.class).getAllCountryTranslations(countryTranslationDto.getCountryId(), request)).withRel("allTranslations")
        );
    }

    public CollectionModel<EntityModel<CountryTranslationDto>> toCollectionModel(Iterable<? extends CountryTranslationDto> countryTranslationsDto, HttpServletRequest request) {
        List<EntityModel<CountryTranslationDto>> countryTranslationsModel = StreamSupport.stream(countryTranslationsDto.spliterator(), false)
                .map(countryTranslationDto -> this.toModel(countryTranslationDto, request))
                .toList();
        return CollectionModel.of(countryTranslationsModel,
                linkTo(methodOn(CountryTranslationAdminController.class).getCountryTranslationById(countryTranslationsDto.iterator().next().getId(), request)).withSelfRel(),
                linkTo(methodOn(CountryTranslationAdminController.class).getAllCountryTranslations(countryTranslationsDto.iterator().next().getCountryId(), request)).withRel("allTranslations")
        );
    }
}
