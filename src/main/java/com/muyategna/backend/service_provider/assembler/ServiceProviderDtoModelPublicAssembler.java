package com.muyategna.backend.service_provider.assembler;

import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class ServiceProviderDtoModelPublicAssembler {
    public EntityModel<ServiceProviderDto> toModel(ServiceProviderDto serviceProviderDto,
                                                   HttpServletRequest request) {
        return EntityModel.of(serviceProviderDto);
    }


    public CollectionModel<EntityModel<ServiceProviderDto>> toCollectionModel(Iterable<? extends ServiceProviderDto> serviceProviderDtos,
                                                                              HttpServletRequest request) {
        List<EntityModel<ServiceProviderDto>> serviceProviderDtoModels = StreamSupport
                .stream(serviceProviderDtos.spliterator(), false)
                .map(serviceProviderDto -> toModel(serviceProviderDto, request))
                .toList();
        return CollectionModel.of(serviceProviderDtoModels);
    }
}
