package com.muyategna.backend.job_request.mapper;

import com.muyategna.backend.job_request.dto.job_lead.JobLeadDto;
import com.muyategna.backend.job_request.entity.JobLead;

public final class JobLeadMapper {
    public static JobLeadDto toDto(JobLead jobLead) {
        if (jobLead == null) {
            return null;
        }
        return JobLeadDto.builder()
                .id(jobLead.getId())
                .jobRequestId(jobLead.getJobRequest().getId())
                .maxClaims(jobLead.getMaxClaims())
                .claimedCount(jobLead.getClaimedCount())
                .createdAt(jobLead.getCreatedAt())
                .updatedAt(jobLead.getUpdatedAt())
                .status(jobLead.getStatus())
                .build();
    }

    public static JobLead toEntity(JobLeadDto jobLead) {
        if (jobLead == null) {
            return null;
        }
        JobLead entity = new JobLead();
        entity.setId(jobLead.getId());
        entity.setMaxClaims(jobLead.getMaxClaims());
        entity.setClaimedCount(jobLead.getClaimedCount());
        entity.setCreatedAt(jobLead.getCreatedAt());
        entity.setUpdatedAt(jobLead.getUpdatedAt());
        entity.setStatus(jobLead.getStatus());
        return entity;
    }
}
