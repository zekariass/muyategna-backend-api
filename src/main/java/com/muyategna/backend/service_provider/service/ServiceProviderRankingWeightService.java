package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.service_provider.entity.ServiceProviderRankingWeight;

import java.util.Optional;

public interface ServiceProviderRankingWeightService {
//    ServiceProviderRankingWeight refreshRankingWeightsCache();

    Optional<ServiceProviderRankingWeight> getRankingWeights();
}
