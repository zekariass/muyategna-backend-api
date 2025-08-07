package com.muyategna.backend.job_request.dto.job_lead;

import com.muyategna.backend.job_request.dto.job_request.JobRequestDetailDto;
import com.muyategna.backend.job_request.enums.JobLeadStatus;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobLeadDetailDto {
    private Long id;
    private JobRequestDetailDto jobRequest;
    private JobLeadStatus status;
}
