package com.muyategna.backend.service_provider.assembler;

import com.muyategna.backend.service_provider.dto.service_provider.NearbyServiceProviderListDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class ServiceProviderListDtoModelPublicAssembler {
    public EntityModel<NearbyServiceProviderListDto> toModel(NearbyServiceProviderListDto nearbyServiceProviderListDto, HttpServletRequest request) {
        return EntityModel.of(nearbyServiceProviderListDto);
    }


    public CollectionModel<EntityModel<NearbyServiceProviderListDto>> toCollectionModel(Iterable<? extends NearbyServiceProviderListDto> serviceProviderListDtos, HttpServletRequest request) {
        List<EntityModel<NearbyServiceProviderListDto>> serviceProviderListDtoModels = StreamSupport
                .stream(serviceProviderListDtos.spliterator(), false)
                .map(serviceProviderListDto -> toModel(serviceProviderListDto, request))
                .toList();
        return CollectionModel.of(serviceProviderListDtoModels);
    }
}
