package com.muyategna.backend.job_request.assembler;

import com.muyategna.backend.job_request.dto.job_quote.JobQuoteDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class JobQuoteDtoModelAssembler {
    public EntityModel<JobQuoteDto> toModel(JobQuoteDto jobQuoteDto, HttpServletRequest request) {
        return EntityModel.of(jobQuoteDto);
    }

    public CollectionModel<EntityModel<JobQuoteDto>> toCollectionModel(Iterable<? extends JobQuoteDto> jobQuoteDtos, HttpServletRequest request) {
        return CollectionModel.of(StreamSupport.stream(jobQuoteDtos.spliterator(), false)
                .map(jobQuoteDto -> toModel(jobQuoteDto, request))
                .collect(Collectors.toList()));
    }
}
