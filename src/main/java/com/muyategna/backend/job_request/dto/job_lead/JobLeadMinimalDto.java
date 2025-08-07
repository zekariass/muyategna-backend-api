package com.muyategna.backend.job_request.dto.job_lead;

import com.muyategna.backend.job_request.dto.job_request.JobRequestMinimalDto;
import com.muyategna.backend.job_request.enums.JobLeadStatus;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobLeadMinimalDto {
    private Long id;
    private JobRequestMinimalDto jobRequest;
    private JobLeadStatus status;
}
