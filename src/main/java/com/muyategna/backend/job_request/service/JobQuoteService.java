package com.muyategna.backend.job_request.service;

import com.muyategna.backend.job_request.dto.job_quote.JobQuoteCreateDto;
import com.muyategna.backend.job_request.dto.job_quote.JobQuoteDto;
import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.job_request.entity.JobQuote;

import java.util.List;

public interface JobQuoteService {
    JobQuote createJobQuote(JobQuoteCreateDto jobQuoteCreateDto, JobLeadClaim jobLeadClaim);

    JobQuoteDto createNewJobQuoteAfterClaim(JobQuoteCreateDto jobQuoteCreateDto, Long serviceProviderId);

    List<JobQuoteDto> getJobQuotesByClaimIdAndServiceProviderId(Long claimId, Long serviceProviderId);

    void requestRequote(Long leadClaimId);
}
