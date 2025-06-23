package com.muyategna.backend.service_provider.assembler;

import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceLocalizedDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class ServiceProviderServiceLocalizedDtoModelPublicAssembler {
    public EntityModel<ServiceProviderServiceLocalizedDto> toModel(ServiceProviderServiceLocalizedDto serviceProviderServiceDto,
                                                                   HttpServletRequest request) {
        return EntityModel.of(serviceProviderServiceDto);
    }


    public CollectionModel<EntityModel<ServiceProviderServiceLocalizedDto>> toCollectionModel(Iterable<? extends ServiceProviderServiceLocalizedDto> serviceProviderServiceDtoList,
                                                                                              HttpServletRequest request) {
        List<EntityModel<ServiceProviderServiceLocalizedDto>> serviceProviderServiceDtoModels = StreamSupport
                .stream(serviceProviderServiceDtoList.spliterator(), false)
                .map(serviceProviderServiceDto -> toModel(serviceProviderServiceDto, request))
                .toList();
        return CollectionModel.of(serviceProviderServiceDtoModels);
    }
}
