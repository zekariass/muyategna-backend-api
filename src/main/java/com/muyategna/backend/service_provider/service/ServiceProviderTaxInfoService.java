package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.service_provider.dto.service_provider_tax_info.ServiceProviderTaxInfoDto;
import com.muyategna.backend.service_provider.dto.service_provider_tax_info.ServiceProviderTaxInfoUpdateDto;
import com.muyategna.backend.service_provider.entity.ServiceProviderTaxInfo;
import jakarta.validation.Valid;

public interface ServiceProviderTaxInfoService {
    ServiceProviderTaxInfo saveTaxInfo(ServiceProviderTaxInfo savedTaxInfo);

    ServiceProviderTaxInfoDto getServiceProviderTaxInfoAndServiceProviderId(Long serviceProviderTaxInfoId, Long serviceProviderId);

    ServiceProviderTaxInfoDto updateServiceProviderTaxInfo(Long serviceProviderId,
                                                           Long serviceProviderTaxInfoId,
                                                           @Valid ServiceProviderTaxInfoUpdateDto serviceProviderTaxInfoDto);
}
