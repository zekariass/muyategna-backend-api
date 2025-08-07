package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.service_credit_cost.ServiceCreditCostDto;
import com.muyategna.backend.billing.mapper.ServiceCreditCostMapper;
import com.muyategna.backend.billing.repository.ServiceCreditCostRepository;
import com.muyategna.backend.redis.CacheConstants;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ServiceCreditCostServiceImpl implements ServiceCreditCostService {

    private final ServiceCreditCostRepository serviceCreditCostRepository;

    @Autowired
    public ServiceCreditCostServiceImpl(ServiceCreditCostRepository serviceCreditCostRepository) {
        this.serviceCreditCostRepository = serviceCreditCostRepository;
    }

    @Cacheable(value = CacheConstants.CACHE_BILLING,
            key = "T(com.muyategna.backend.redis.BillingCacheKeyUtil).getServiceCreditCostKey(#serviceId)",
            unless = "#result == null"
    )
    @Override
    public ServiceCreditCostDto findByServiceId(Long serviceId) {
        log.info("Retrieving service credit cost for service with ID: {}", serviceId);
        return serviceCreditCostRepository.findByServiceId(serviceId)
                .map(ServiceCreditCostMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Service credit cost not found for service ID: " + serviceId));
    }
}
