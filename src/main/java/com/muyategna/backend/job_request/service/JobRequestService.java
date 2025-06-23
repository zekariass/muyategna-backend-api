package com.muyategna.backend.job_request.service;

import com.muyategna.backend.job_request.dto.job_request.JobRequestCreateDto;
import com.muyategna.backend.job_request.dto.job_request.JobRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface JobRequestService {
    JobRequestDto createJobRequest(JobRequestCreateDto jobRequestCreateDto);
}
