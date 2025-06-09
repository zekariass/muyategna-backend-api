package com.muyategna.backend.location.assembler;

import com.muyategna.backend.location.controller.admin.CountryAdminController;
import com.muyategna.backend.location.dto.country.CountryDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CountryDtoModelAdminAssembler {

    public EntityModel<CountryDto> toModel(CountryDto countryDto, HttpServletRequest request) {
        return EntityModel.of(
                countryDto,
                linkTo(methodOn(CountryAdminController.class).getCountryById(countryDto.getId(), request)).withSelfRel(),
                linkTo(methodOn(CountryAdminController.class).getAllCountries(request)).withRel("allCountries")
        );
    }

    public CollectionModel<EntityModel<CountryDto>> toCollectionModel(Iterable<? extends CountryDto> countries, HttpServletRequest request) {
        List<EntityModel<CountryDto>> countriesModel = StreamSupport.stream(countries.spliterator(), false)
                .map(countryDto -> this.toModel(countryDto, request))
                .toList();

        return CollectionModel.of(
                countriesModel,
                linkTo(methodOn(CountryAdminController.class).getAllCountries(request)).withSelfRel());
    }
}
