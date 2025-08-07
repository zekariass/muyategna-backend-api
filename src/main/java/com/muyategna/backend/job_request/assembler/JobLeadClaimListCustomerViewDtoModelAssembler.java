package com.muyategna.backend.job_request.assembler;

import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimListCustomerViewDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class JobLeadClaimListCustomerViewDtoModelAssembler {

    public EntityModel<JobLeadClaimListCustomerViewDto> toModel(JobLeadClaimListCustomerViewDto jobLeadClaimListCustomerViewDto, HttpServletRequest request) {
        return EntityModel.of(jobLeadClaimListCustomerViewDto);
    }

    public CollectionModel<EntityModel<JobLeadClaimListCustomerViewDto>> toCollectionModel(Iterable<? extends JobLeadClaimListCustomerViewDto> jobLeadClaims, HttpServletRequest request) {
        return CollectionModel.of(StreamSupport.stream(jobLeadClaims.spliterator(), false)
                .map(jobLeadClaim -> toModel(jobLeadClaim, request))
                .collect(Collectors.toList()));
    }
}
