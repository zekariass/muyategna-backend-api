package com.muyategna.backend.job_request.service;

import com.muyategna.backend.job_request.dto.job_lead.JobLeadDto;
import com.muyategna.backend.job_request.dto.job_lead.JobLeadListDto;
import com.muyategna.backend.job_request.entity.JobRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobLeadService {
    void createJobLeadForNewUserJobRequest(JobRequest jobRequest);

    Page<JobLeadListDto> getAllJobLeadsClosestToTheServiceProvider(Long serviceProviderId, Pageable pageable);

    JobLeadDto getJobLeadById(Long jobLeadId);
}
