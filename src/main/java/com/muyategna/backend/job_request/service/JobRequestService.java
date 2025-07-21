package com.muyategna.backend.job_request.service;

import com.muyategna.backend.job_request.dto.job_request.*;
import org.springframework.stereotype.Service;

@Service
public interface JobRequestService {
    JobRequestDto createJobRequestForNewUser(JobRequestByNewUserWithJsonStringCreateDto jobRequestCreateJsonDto);
    JobRequestDto createJobRequestForExistingUser(JobRequestByExistingUserWithJsonStringCreateDto jobRequestCreateDto);

    JobRequestDto getJobRequestById(Long jobRequestId);
    JobRequestDetailDto getJobRequestDetailById(Long jobRequestId);

    JobRequestDto updateJobRequest(Long jobRequestId, JobRequestUpdateDto updateDto);

    void cancelJobRequest(Long jobRequestId);

    void reopenJobRequest(Long jobRequestId);
}
