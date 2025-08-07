package com.muyategna.backend.job_request.algorithm;

import com.muyategna.backend.job_request.entity.JobRequest;
import com.muyategna.backend.service_provider.repository.projection.NearbyServiceProviderForAJobProjection;

import java.util.List;

public interface JobMatchingAlgorithmService {
    List<Long> getTopNServiceProviders(Long jobRequestId, int topN, Long countryId, int queryLimit);

    List<NearbyServiceProviderForAJobProjection> getNearbyServiceProviders(JobRequest jobRequest, double radiusMeters, int queryLimit, int numOfProviders, Long countryId);
}
