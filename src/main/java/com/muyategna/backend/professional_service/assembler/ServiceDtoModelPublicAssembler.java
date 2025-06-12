package com.muyategna.backend.professional_service.assembler;

import com.muyategna.backend.professional_service.controller.ServicePublicController;
import com.muyategna.backend.professional_service.dto.service.ServiceLocalizedDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ServiceDtoModelPublicAssembler {

    public EntityModel<ServiceLocalizedDto> toModel(ServiceLocalizedDto serviceLocalizedDto, HttpServletRequest request) {
        return EntityModel.of(
                serviceLocalizedDto,
                linkTo(methodOn(ServicePublicController.class).getServicesByCategoryForCurrentCountry(serviceLocalizedDto.getId(), request)).withSelfRel()
        );
    }

    public CollectionModel<EntityModel<ServiceLocalizedDto>> toCollectionModel(Iterable<? extends ServiceLocalizedDto> serviceDtoList, HttpServletRequest request) {
        List<EntityModel<ServiceLocalizedDto>> serviceCategoriesModel = StreamSupport.stream(serviceDtoList.spliterator(), false)
                .map(dto -> this.toModel(dto, request))
                .toList();
        return CollectionModel.of(serviceCategoriesModel);
    }
}
