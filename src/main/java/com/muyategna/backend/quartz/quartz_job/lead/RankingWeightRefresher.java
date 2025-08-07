//package com.muyategna.backend.quartz.quartz_job.lead;
//
//import com.muyategna.backend.service_provider.entity.ServiceProviderRankingWeight;
//import com.muyategna.backend.service_provider.service.ServiceProviderRankingWeightService;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class RankingWeightRefresher implements Job {
//
//    private final ServiceProviderRankingWeightService serviceProviderRankingWeightService;
//
//    @Autowired
//    public RankingWeightRefresher(ServiceProviderRankingWeightService serviceProviderRankingWeightService) {
//        this.serviceProviderRankingWeightService = serviceProviderRankingWeightService;
//    }
//
//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        ServiceProviderRankingWeight weight = serviceProviderRankingWeightService.refreshRankingWeightsCache();
//    }
//}
