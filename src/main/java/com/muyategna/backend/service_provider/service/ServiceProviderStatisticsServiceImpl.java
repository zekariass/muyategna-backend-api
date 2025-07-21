package com.muyategna.backend.service_provider.service;

import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.entity.ServiceProviderStatistics;
import com.muyategna.backend.service_provider.repository.ServiceProviderStatisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class ServiceProviderStatisticsServiceImpl implements ServiceProviderStatisticsService {

    private final ServiceProviderStatisticsRepository serviceProviderStatisticsRepository;

    @Autowired
    public ServiceProviderStatisticsServiceImpl(ServiceProviderStatisticsRepository serviceProviderStatisticsRepository) {
        this.serviceProviderStatisticsRepository = serviceProviderStatisticsRepository;
    }

    @Override
    public ServiceProviderStatistics createDefaultStatistics(Long serviceProviderId) {
        ServiceProviderStatistics statistics = new ServiceProviderStatistics();
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setId(serviceProviderId);

        statistics.setProvider(serviceProvider);
//        statistics.setAvgResponseTime(BigDecimal.ZERO);
        statistics.setAvgRating(BigDecimal.ZERO);
//        statistics.setTotalLeads(0);
//        statistics.setRespondedLeads(0);
        statistics.setCompletedLeads(0);
//        statistics.setResponseRate(BigDecimal.ZERO);
//        statistics.setRejectionRate(BigDecimal.ZERO);
//        statistics.setDayLeadLoad(0);
//        statistics.setWeekLeadLoad(0);
//        statistics.setTwoWeekLeadLoad(0);
//        statistics.setThreeWeekLeadLoad(0);
//        statistics.setMonthLeadLoad(0);

        return serviceProviderStatisticsRepository.save(statistics);
    }


    @Override
    public void updateStatisticsForNewLeadAssignment(Long serviceProviderId) {
        ServiceProviderStatistics statistics = serviceProviderStatisticsRepository.findByProviderId(serviceProviderId);
//        statistics.setTotalLeads(statistics.getTotalLeads() + 1);
//        statistics.setDayLeadLoad(statistics.getDayLeadLoad() + 1);
//        statistics.setWeekLeadLoad(statistics.getWeekLeadLoad() + 1);
//        statistics.setTwoWeekLeadLoad(statistics.getTwoWeekLeadLoad() + 1);
//        statistics.setThreeWeekLeadLoad(statistics.getThreeWeekLeadLoad() + 1);
//        statistics.setMonthLeadLoad(statistics.getMonthLeadLoad() + 1);

        serviceProviderStatisticsRepository.save(statistics);
    }
}
