package com.muyategna.backend.job_request.mapper;

import com.muyategna.backend.job_request.dto.job_quote.JobQuoteCreateDto;
import com.muyategna.backend.job_request.dto.job_quote.JobQuoteDto;
import com.muyategna.backend.job_request.dto.job_quote.JobQuoteUpdateDto;
import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.job_request.entity.JobQuote;

import java.util.List;

public final class JobQuoteMapper {

    public static JobQuoteDto toDto(JobQuote entity) {
        return JobQuoteDto.builder()
                .id(entity.getId())
                .claimId(entity.getClaim().getId())
                .proposedPrice(entity.getProposedPrice())
                .proposedStartDate(entity.getProposedStartDate())
                .message(entity.getMessage())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static JobQuote toEntity(JobQuoteCreateDto dto, JobLeadClaim claim) {
        JobQuote entity = new JobQuote();
        entity.setClaim(claim);
        entity.setMessage(dto.getMessage());
        entity.setProposedPrice(dto.getProposedPrice());
        entity.setProposedStartDate(dto.getProposedStartDate());
        return entity;
    }

    public static JobQuote toEntity(JobQuoteUpdateDto dto, JobLeadClaim claim) {
        JobQuote entity = new JobQuote();
        entity.setId(dto.getId());
        entity.setClaim(claim);
        entity.setMessage(dto.getMessage());
        entity.setProposedPrice(dto.getProposedPrice());
        entity.setProposedStartDate(dto.getProposedStartDate());
        return entity;
    }

    public static List<JobQuoteDto> toDtoList(List<JobQuote> entities) {
        return entities.stream().map(JobQuoteMapper::toDto).toList();
    }
}
