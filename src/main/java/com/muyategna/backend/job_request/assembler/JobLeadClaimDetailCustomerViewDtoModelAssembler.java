package com.muyategna.backend.job_request.assembler;

import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimDetailCustomerViewDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class JobLeadClaimDetailCustomerViewDtoModelAssembler {

    public EntityModel<JobLeadClaimDetailCustomerViewDto> toModel(JobLeadClaimDetailCustomerViewDto jobLeadClaimDetailCustomerViewDto, HttpServletRequest request) {
        return EntityModel.of(jobLeadClaimDetailCustomerViewDto);
    }

    public CollectionModel<EntityModel<JobLeadClaimDetailCustomerViewDto>> toCollectionModel(Iterable<? extends JobLeadClaimDetailCustomerViewDto> jobLeadClaimDetailCustomerViewDtos, HttpServletRequest request) {
        return CollectionModel.of(StreamSupport.stream(jobLeadClaimDetailCustomerViewDtos.spliterator(), false)
                .map(jobLeadClaimDetailCustomerViewDto -> toModel(jobLeadClaimDetailCustomerViewDto, request))
                .collect(Collectors.toList()));
    }
}
