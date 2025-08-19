package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.PlatformBankInformation;
import com.muyategna.backend.billing.gateways.dto.platform_bank_information.PlatformBankInformationViewDto;
import com.muyategna.backend.billing.gateways.mappers.platform_bank_information.PlatformBankInformationMapper;
import com.muyategna.backend.billing.repository.PlatformBankInformationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PlatformBankInformationServiceImpl implements PlatformBankInformationService {

    private final PlatformBankInformationRepository repository;

    @Autowired
    public PlatformBankInformationServiceImpl(PlatformBankInformationRepository repository) {
        this.repository = repository;
    }


    //    @Cacheable(
//            value = CacheConstants.CACHE_BILLING,
//            key = "T(com.muyategna.backend.redis.BillingCacheKeyUtil).getPlatformBankInformationKey(#countryId, #isActive)",
//            unless = "#result == null"
//    )
    @Override
    public List<PlatformBankInformationViewDto> getPlatformBankInformationByCountryIdAndIsActive(Long countryId, boolean isActive) {
        log.info("Fetching platform bank information for country: {} with active status: {}", countryId, isActive);

        List<PlatformBankInformation> platformBankInformationList =
                repository.findByCountryIdAndIsActive(countryId, isActive);

        log.info("Fetched {} platform bank information for country: {} with active status: {}",
                platformBankInformationList.size(), countryId, isActive);

        return PlatformBankInformationMapper.toViewDtoList(platformBankInformationList);
    }

}
