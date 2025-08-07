package com.muyategna.backend.job_request.assembler;

import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimSPListViewDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class JobLeadClaimSPListDtoModelAssembler {
    public EntityModel<JobLeadClaimSPListViewDto> toModel(JobLeadClaimSPListViewDto jobLeadClaimSPListViewDto, HttpServletRequest request) {
        return EntityModel.of(jobLeadClaimSPListViewDto);
    }

    public CollectionModel<EntityModel<JobLeadClaimSPListViewDto>> toCollectionModel(Iterable<? extends JobLeadClaimSPListViewDto> jobLeadClaimSPListDtos, HttpServletRequest request) {
        return CollectionModel.of(StreamSupport.stream(jobLeadClaimSPListDtos.spliterator(), false)
                .map(jobLeadClaimSPListDto -> toModel(jobLeadClaimSPListDto, request))
                .collect(Collectors.toList()));
    }
}
