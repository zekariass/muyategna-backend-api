package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.gateways.dto.platform_bank_information.PlatformBankInformationViewDto;

import java.util.List;

public interface PlatformBankInformationService {
    List<PlatformBankInformationViewDto> getPlatformBankInformationByCountryIdAndIsActive(Long countryId, boolean isActive);
}
