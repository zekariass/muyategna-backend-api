package com.muyategna.backend.job_request.assembler;

import com.muyategna.backend.job_request.dto.job_lead.JobLeadListDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class JobLeadListDtoModelAssembler {
    public EntityModel<JobLeadListDto> toModel(JobLeadListDto jobLeadListDto, Long serviceProviderId, Integer page, Integer size, String sortBy, HttpServletRequest request) { // Convert JobLeadListDto to EntityModel<JobLeadListDto>
        return EntityModel.of(jobLeadListDto);
    }


    public CollectionModel<EntityModel<JobLeadListDto>> toCollectionModel(Iterable<? extends JobLeadListDto> jobLeads, Long serviceProviderId, Integer page, Integer size, String sortBy, HttpServletRequest request) {
        List<EntityModel<JobLeadListDto>> jobLeadsModel = StreamSupport.stream(jobLeads.spliterator(), false)
                .map(jobLeadDto -> this.toModel(jobLeadDto, serviceProviderId, page, size, sortBy, request))
                .toList();
        return CollectionModel.of(jobLeadsModel);
    }
}
