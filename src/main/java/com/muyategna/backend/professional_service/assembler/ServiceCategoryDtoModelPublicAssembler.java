package com.muyategna.backend.professional_service.assembler;

import com.muyategna.backend.professional_service.dto.service_category.ServiceCategoryLocalizedDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class ServiceCategoryDtoModelPublicAssembler {

    public EntityModel<ServiceCategoryLocalizedDto> toModel(ServiceCategoryLocalizedDto serviceCategoryDto, HttpServletRequest request) {
        return EntityModel.of(serviceCategoryDto);
    }

    public CollectionModel<EntityModel<ServiceCategoryLocalizedDto>> toCollectionModel(Iterable<? extends ServiceCategoryLocalizedDto> serviceCategoriesDto, HttpServletRequest request) {
        List<EntityModel<ServiceCategoryLocalizedDto>> serviceCategoriesModel = StreamSupport.stream(serviceCategoriesDto.spliterator(), false)
                .map(serviceCategoryDto -> this.toModel(serviceCategoryDto, request))
                .toList();
        return CollectionModel.of(serviceCategoriesModel);
    }
}
