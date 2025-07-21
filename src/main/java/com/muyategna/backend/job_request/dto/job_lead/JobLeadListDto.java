package com.muyategna.backend.job_request.dto.job_lead;

import com.muyategna.backend.job_request.dto.job_request.JobRequestListDto;
import com.muyategna.backend.job_request.enums.JobLeadStatus;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobLeadListDto {
    private Long id;
    private JobRequestListDto jobRequest;
    private JobLeadStatus status;
    private Integer maxClaims;
    private Integer claimedCount;
    private Double distanceMeters;
    private LocalDateTime createdAt;
}
