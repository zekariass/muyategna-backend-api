package com.muyategna.backend.job_request.mapper;

import com.muyategna.backend.job_request.dto.job_lead.JobLeadDetailDto;
import com.muyategna.backend.job_request.dto.job_lead.JobLeadDto;
import com.muyategna.backend.job_request.dto.job_lead.JobLeadListDto;
import com.muyategna.backend.job_request.dto.job_lead.JobLeadMinimalDto;
import com.muyategna.backend.job_request.dto.job_request.JobRequestListDto;
import com.muyategna.backend.job_request.entity.JobLead;
import com.muyategna.backend.job_request.repository.projection.JobLeadProjection;
import com.muyategna.backend.professional_service.entity.ServiceTranslation;

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


    public static JobLeadListDto toListDto(JobLeadProjection jobLeadProjection, JobRequestListDto jobRequestListDto) {
        if (jobLeadProjection == null) {
            return null;
        }
        return JobLeadListDto.builder()
                .id(jobLeadProjection.getId())
                .jobRequest(jobRequestListDto)
                .maxClaims(jobLeadProjection.getMaxClaims())
                .claimedCount(jobLeadProjection.getClaimedCount())
                .distanceMeters(jobLeadProjection.getDistanceMeters())
                .createdAt(jobLeadProjection.getCreatedAt())
                .status(jobLeadProjection.getStatus())
                .build();
    }


    public static JobLeadMinimalDto toMinimalDto(JobLead jobLead, ServiceTranslation translation) {
        if (jobLead == null) {
            return null;
        }
        return JobLeadMinimalDto.builder()
                .id(jobLead.getId())
                .jobRequest(JobRequestMapper.toMinimalDto(jobLead.getJobRequest(), translation))
                .status(jobLead.getStatus())
                .build();
    }

    public static JobLeadDetailDto toDetailDto(JobLead jobLead, ServiceTranslation translation) {
        if (jobLead == null) {
            return null;
        }
        return JobLeadDetailDto.builder()
                .id(jobLead.getId())
                .jobRequest(JobRequestMapper.toDetailDto(jobLead.getJobRequest(), translation))
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
