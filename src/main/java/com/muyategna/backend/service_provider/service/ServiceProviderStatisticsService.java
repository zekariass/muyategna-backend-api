package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.service_provider.entity.ServiceProviderStatistics;

import java.util.List;

public interface ServiceProviderStatisticsService {
    ServiceProviderStatistics createDefaultStatistics(Long serviceProviderId);

    ServiceProviderStatistics createStatisticsWithoutSave(Long serviceProviderId);

    void updateStatisticsForNewLeadAssignment(Long serviceProviderId);

    List<ServiceProviderStatistics> getServiceProviderStatisticsByServiceProviderIds(List<Long> serviceProviderIds);
}
