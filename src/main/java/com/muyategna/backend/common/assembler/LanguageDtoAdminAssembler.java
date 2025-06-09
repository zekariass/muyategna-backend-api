package com.muyategna.backend.common.assembler;

import com.muyategna.backend.common.controller.admin.LanguageAdminController;
import com.muyategna.backend.common.dto.language.LanguageDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembler for converting LanguageDto objects to EntityModel and CollectionModel.
 * <p>
 * This class provides methods to convert a single LanguageDto to an EntityModel
 * and a collection of LanguageDto objects to a CollectionModel, adding appropriate links.
 * </p>
 */
@Component
public class LanguageDtoAdminAssembler {

    /**
     * Converts a LanguageDto to an EntityModel with self and related links.
     *
     * @param languageDto the LanguageDto to convert
     * @param request     the HttpServletRequest for generating links
     * @return an EntityModel containing the LanguageDto and its links
     */
    public EntityModel<LanguageDto> toModel(LanguageDto languageDto, HttpServletRequest request) {
        return EntityModel.of(
                languageDto,
                linkTo(methodOn(LanguageAdminController.class).getLanguageById(languageDto.getId(), request)).withSelfRel(),
                linkTo(methodOn(LanguageAdminController.class).getAllLanguages(request)).withRel("allLanguages"),
                linkTo(methodOn(LanguageAdminController.class).getLanguagesByCountryId(languageDto.getCountryId(), request)).withRel("country")
        );
    }

    /**
     * Converts an iterable of LanguageDto objects to a CollectionModel.
     *
     * @param languagesDto the iterable of LanguageDto objects to convert
     * @param request      the HttpServletRequest for generating links
     * @return a CollectionModel containing EntityModels of the LanguageDto objects and their links
     */
    public CollectionModel<EntityModel<LanguageDto>> toCollectionModel(Iterable<? extends LanguageDto> languagesDto, HttpServletRequest request) {
        List<EntityModel<LanguageDto>> languagesModel = StreamSupport.stream(languagesDto.spliterator(), false)
                .map(languageDto -> this.toModel(languageDto, request))
                .toList();
        return CollectionModel.of(languagesModel,
                linkTo(methodOn(LanguageAdminController.class).getAllLanguages(request)).withSelfRel());
    }
}
