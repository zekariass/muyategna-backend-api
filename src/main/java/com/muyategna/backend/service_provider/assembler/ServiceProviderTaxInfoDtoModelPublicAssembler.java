package com.muyategna.backend.service_provider.assembler;

import com.muyategna.backend.service_provider.dto.service_provider_tax_info.ServiceProviderTaxInfoDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

@Component
public class ServiceProviderTaxInfoDtoModelPublicAssembler {

    public EntityModel<ServiceProviderTaxInfoDto> toModel(ServiceProviderTaxInfoDto serviceProviderTaxInfoDto,
                                                          HttpServletRequest request) {
        return EntityModel.of(serviceProviderTaxInfoDto);
    }
}
