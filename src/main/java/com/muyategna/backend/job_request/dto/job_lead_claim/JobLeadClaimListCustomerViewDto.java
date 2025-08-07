package com.muyategna.backend.job_request.dto.job_lead_claim;

import com.muyategna.backend.job_request.dto.job_lead.JobLeadMinimalDto;
import com.muyategna.backend.job_request.enums.JobLeadClaimStatus;
import com.muyategna.backend.service_provider.dto.service_provider.ServiceProviderListCustomerViewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobLeadClaimListCustomerViewDto {
    private Long id;
    private JobLeadMinimalDto jobLead;
    private ServiceProviderListCustomerViewDto provider;
    private LocalDateTime claimedAt;
    private JobLeadClaimStatus status;
    private Boolean requoteRequested;
}
