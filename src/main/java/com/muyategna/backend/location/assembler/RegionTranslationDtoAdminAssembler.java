package com.muyategna.backend.location.assembler;

import com.muyategna.backend.location.controller.admin.RegionTranslationAdminController;
import com.muyategna.backend.location.dto.region.RegionTranslationDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RegionTranslationDtoAdminAssembler {

    public EntityModel<RegionTranslationDto> toModel(RegionTranslationDto regionTranslationDto, HttpServletRequest request) {
        return EntityModel.of(
                regionTranslationDto,
                linkTo(methodOn(RegionTranslationAdminController.class).getRegionTranslationById(regionTranslationDto.getId(), request)).withSelfRel(),
                linkTo(methodOn(RegionTranslationAdminController.class).getRegionTranslationsByRegionId(regionTranslationDto.getRegionId(), request)).withRel("AllRegionTranslations")
        );
    }

    public CollectionModel<EntityModel<RegionTranslationDto>> toCollectionModel(Iterable<? extends RegionTranslationDto> regionTranslationDtoList, HttpServletRequest request) {
        List<EntityModel<RegionTranslationDto>> regionTranslationsModel = StreamSupport.stream(regionTranslationDtoList.spliterator(), false)
                .map(regionTranslationDto -> this.toModel(regionTranslationDto, request))
                .toList();
        return CollectionModel.of(regionTranslationsModel,
                linkTo(methodOn(RegionTranslationAdminController.class).getRegionTranslationById(regionTranslationDtoList.iterator().next().getId(), request)).withSelfRel(),
                linkTo(methodOn(RegionTranslationAdminController.class).getRegionTranslationsByRegionId(regionTranslationDtoList.iterator().next().getRegionId(), request)).withRel("AllRegionTranslations")
        );
    }
}
