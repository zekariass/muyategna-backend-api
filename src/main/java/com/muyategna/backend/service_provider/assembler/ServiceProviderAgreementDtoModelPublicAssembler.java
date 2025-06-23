package com.muyategna.backend.service_provider.assembler;

import com.muyategna.backend.service_provider.dto.service_provider_agreement.ServiceProviderAgreementDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class ServiceProviderAgreementDtoModelPublicAssembler {
    public EntityModel<ServiceProviderAgreementDto> toModel(ServiceProviderAgreementDto serviceProviderAgreementDto,
                                                            HttpServletRequest request) {
        return EntityModel.of(serviceProviderAgreementDto);
    }


    public CollectionModel<EntityModel<ServiceProviderAgreementDto>> toCollectionModel(Iterable<? extends ServiceProviderAgreementDto> serviceProviderAgreementDtoList,
                                                                                       HttpServletRequest request) {
        List<EntityModel<ServiceProviderAgreementDto>> serviceProviderAgreementDtoModels = StreamSupport
                .stream(serviceProviderAgreementDtoList.spliterator(), false)
                .map(serviceProviderAgreementDto -> toModel(serviceProviderAgreementDto, request))
                .toList();
        return CollectionModel.of(serviceProviderAgreementDtoModels);
    }
}
