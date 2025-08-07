package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.redis.CacheConstants;
import com.muyategna.backend.service_provider.entity.ServiceProviderRankingWeight;
import com.muyategna.backend.service_provider.repository.ServiceProviderRankingWeightRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ServiceProviderRankingWeightServiceImpl implements ServiceProviderRankingWeightService {

    private final ServiceProviderRankingWeightRepository serviceProviderRankingWeightRepository;

    @Autowired
    public ServiceProviderRankingWeightServiceImpl(ServiceProviderRankingWeightRepository serviceProviderRankingWeightRepository) {
        this.serviceProviderRankingWeightRepository = serviceProviderRankingWeightRepository;
    }

//    @CachePut(value = CacheConstants.CACHE_SERVICE_PROVIDER,
//            key = "T(com.muyategna.backend.redis.ServiceProviderCacheKeyUtil).getServiceProviderRankingWeightsKey()"
//    )
//    @Override
//    public ServiceProviderRankingWeight refreshRankingWeightsCache() {
//        log.info("Refreshing Service Provider Ranking Weights Cache");
//        return serviceProviderRankingWeightRepository.findByIsActive(true);

    /// /                .orElseThrow(() -> new RuntimeException("Service Provider Ranking Weights not found"));
//    }
    @Cacheable(
            value = CacheConstants.CACHE_SERVICE_PROVIDER,
            key = "T(com.muyategna.backend.redis.ServiceProviderCacheKeyUtil).getServiceProviderRankingWeightsKey()",
            unless = "#result == null"
    )
    @Override
    public Optional<ServiceProviderRankingWeight> getRankingWeights() {
        log.info("Getting Service Provider Ranking Weights from Cache");
        return serviceProviderRankingWeightRepository.findByIsActive(true);
    }
}
