package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.CreditUsage;
import com.muyategna.backend.billing.enums.CreditSource;
import com.muyategna.backend.billing.repository.CreditUsageRepository;
import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class CreditUsageServiceImpl implements CreditUsageService {

    private final CreditUsageRepository creditUsageRepository;

    @Autowired
    public CreditUsageServiceImpl(CreditUsageRepository creditUsageRepository) {
        this.creditUsageRepository = creditUsageRepository;
    }

    @Override
    public void createCreditUsage(ServiceProvider serviceProvider, JobLeadClaim savedJobLeadClaim, BigDecimal usedCreditAmount, CreditSource creditSource, Boolean isPartial) {

//        log.info("Creating CreditUsage for Service Provider with ID: {} and Job Lead Claim with ID: {}", serviceProviderId, savedJobLeadClaim.getId());
//        CreditUsage creditUsage = new CreditUsage();
//        creditUsage.setProvider(serviceProviderId);
//        creditUsage.setJobLeadClaim(savedJobLeadClaim);
//        creditUsage.setUsedAmount(usedCreditAmount);
//        creditUsage.setCreditSource(creditSource);
//        creditUsage.setIsPartial(isPartial);
//        creditUsage.setUsedAt(LocalDateTime.now());

        log.info("Creating CreditUsage for Service Provider with ID: {} and Job Lead Claim with ID: {}", serviceProvider.getId(), savedJobLeadClaim.getId());
        CreditUsage creditUsage = new CreditUsage(serviceProvider, savedJobLeadClaim, usedCreditAmount, creditSource, isPartial);
        creditUsageRepository.save(creditUsage);
        log.info("CreditUsage created for Service Provider with ID: {} and Job Lead Claim with ID: {}", serviceProvider.getId(), savedJobLeadClaim.getId());
    }
}
