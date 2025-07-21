package com.muyategna.backend.job_request.mapper;

import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimDto;
import com.muyategna.backend.job_request.entity.JobLeadClaim;

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
}
