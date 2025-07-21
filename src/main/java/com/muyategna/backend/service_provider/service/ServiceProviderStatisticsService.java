package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.service_provider.entity.ServiceProviderStatistics;

public interface ServiceProviderStatisticsService {
    ServiceProviderStatistics createDefaultStatistics(Long serviceProviderId);

    void updateStatisticsForNewLeadAssignment(Long serviceProviderId);
}
