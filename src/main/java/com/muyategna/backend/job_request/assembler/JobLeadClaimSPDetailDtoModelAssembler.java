package com.muyategna.backend.job_request.assembler;

import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimDetailSPViewDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class JobLeadClaimSPDetailDtoModelAssembler {
    public EntityModel<JobLeadClaimDetailSPViewDto> toModel(JobLeadClaimDetailSPViewDto jobLeadClaimDetailSPViewDto, HttpServletRequest request) {
        return EntityModel.of(jobLeadClaimDetailSPViewDto);
    }

    public CollectionModel<EntityModel<JobLeadClaimDetailSPViewDto>> toCollectionModel(Iterable<? extends JobLeadClaimDetailSPViewDto> jobLeadClaimSPDetailDtos, HttpServletRequest request) {
        return CollectionModel.of(StreamSupport.stream(jobLeadClaimSPDetailDtos.spliterator(), false)
                .map(jobLeadClaimSPDetailDto -> toModel(jobLeadClaimSPDetailDto, request))
                .collect(Collectors.toList()));
    }
}
