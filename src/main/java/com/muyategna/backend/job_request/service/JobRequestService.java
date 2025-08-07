package com.muyategna.backend.job_request.service;

import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimListCustomerViewDto;
import com.muyategna.backend.job_request.dto.job_request.*;
import com.muyategna.backend.service_provider.dto.service_provider.NearbyServiceProviderListDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JobRequestService {
    JobRequestDto createJobRequestForNewUser(JobRequestByNewUserWithJsonStringCreateDto jobRequestCreateJsonDto);

    JobRequestDto createJobRequestForExistingUser(JobRequestByExistingUserWithJsonStringCreateDto jobRequestCreateDto);

    JobRequestDto getJobRequestById(Long jobRequestId);

    JobRequestDetailDto getJobRequestDetailById(Long jobRequestId);

    JobRequestDto updateJobRequest(Long jobRequestId, JobRequestUpdateDto updateDto);

    void cancelJobRequest(Long jobRequestId);

    void reopenJobRequest(Long jobRequestId);

    List<NearbyServiceProviderListDto> getNearbyServiceProvidersForJobRequest(Long jobRequestId);

    List<JobLeadClaimListCustomerViewDto> getAllClaimsOfJobRequestForCustomer(Long jobRequestId);
}
