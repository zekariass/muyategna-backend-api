package com.muyategna.backend.job_request.mapper;

import com.muyategna.backend.job_request.dto.job_lead_claim.*;
import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.professional_service.entity.ServiceTranslation;
import com.muyategna.backend.service_provider.mapper.ServiceProviderMapper;

public final class JobLeadClaimMapper {
    public static JobLeadClaimDto toDto(JobLeadClaim jobLeadClaim) {
        if (jobLeadClaim == null) return null;
        return JobLeadClaimDto.builder()
                .id(jobLeadClaim.getId())
                .jobLeadId(jobLeadClaim.getJobLead().getId())
                .providerId(jobLeadClaim.getProvider().getId())
                .claimedAt(jobLeadClaim.getClaimedAt())
                .status(jobLeadClaim.getStatus())
                .requoteRequested(jobLeadClaim.getRequoteRequested())
                .createdAt(jobLeadClaim.getCreatedAt())
                .updatedAt(jobLeadClaim.getUpdatedAt())
                .build();
    }

    public static JobLeadClaim toEntity(JobLeadClaimDto jobLeadClaimDto) {
        if (jobLeadClaimDto == null) return null;
        JobLeadClaim jobLeadClaim = new JobLeadClaim();
        jobLeadClaim.setId(jobLeadClaimDto.getId());
        jobLeadClaim.setClaimedAt(jobLeadClaimDto.getClaimedAt());
        jobLeadClaim.setStatus(jobLeadClaimDto.getStatus());
        jobLeadClaim.setCreatedAt(jobLeadClaimDto.getCreatedAt());
        jobLeadClaim.setUpdatedAt(jobLeadClaimDto.getUpdatedAt());
        jobLeadClaim.setRequoteRequested(jobLeadClaimDto.getRequoteRequested());
        return jobLeadClaim;
    }


    public static JobLeadClaimSPListViewDto toSPClaimListDto(JobLeadClaim jobLeadClaim, ServiceTranslation translation) {
        if (jobLeadClaim == null) return null;
        return JobLeadClaimSPListViewDto.builder()
                .id(jobLeadClaim.getId())
                .jobLead(JobLeadMapper.toMinimalDto(jobLeadClaim.getJobLead(), translation))
                .providerId(jobLeadClaim.getProvider().getId())
                .claimedAt(jobLeadClaim.getClaimedAt())
                .status(jobLeadClaim.getStatus())
                .requoteRequested(jobLeadClaim.getRequoteRequested())
                .build();
    }


    public static JobLeadClaimDetailSPViewDto toSPClaimDetailDto(JobLeadClaim jobLeadClaim, ServiceTranslation translation) {
        if (jobLeadClaim == null) return null;
        return JobLeadClaimDetailSPViewDto.builder()
                .id(jobLeadClaim.getId())
                .jobLead(JobLeadMapper.toDetailDto(jobLeadClaim.getJobLead(), translation))
                .providerId(jobLeadClaim.getProvider().getId())
                .claimedAt(jobLeadClaim.getClaimedAt())
                .status(jobLeadClaim.getStatus())
                .requoteRequested(jobLeadClaim.getRequoteRequested())
                .quotes(JobQuoteMapper.toMinimalDtoList(jobLeadClaim.getQuotes()))
                .build();
    }


    public static JobLeadClaimListCustomerViewDto toCustomerClaimListDto(JobLeadClaim jobLeadClaim, ServiceTranslation translation) {
        if (jobLeadClaim == null) return null;
        return JobLeadClaimListCustomerViewDto.builder()
                .id(jobLeadClaim.getId())
                .jobLead(JobLeadMapper.toMinimalDto(jobLeadClaim.getJobLead(), translation))
                .provider(ServiceProviderMapper.toServiceProviderCustomerViewDto(jobLeadClaim.getProvider()))
                .claimedAt(jobLeadClaim.getClaimedAt())
                .status(jobLeadClaim.getStatus())
                .requoteRequested(jobLeadClaim.getRequoteRequested())
                .build();
    }


    public static JobLeadClaimDetailCustomerViewDto toCustomerClaimDetailDto(JobLeadClaim jobLeadClaim, ServiceTranslation translation) {
        if (jobLeadClaim == null) return null;
        return JobLeadClaimDetailCustomerViewDto.builder()
                .id(jobLeadClaim.getId())
                .jobLead(JobLeadMapper.toDetailDto(jobLeadClaim.getJobLead(), translation))
                .provider(ServiceProviderMapper.toServiceProviderCustomerViewDto(jobLeadClaim.getProvider()))
                .claimedAt(jobLeadClaim.getClaimedAt())
                .status(jobLeadClaim.getStatus())
                .requoteRequested(jobLeadClaim.getRequoteRequested())
                .quotes(JobQuoteMapper.toMinimalDtoList(jobLeadClaim.getQuotes()))
                .build();
    }
}
