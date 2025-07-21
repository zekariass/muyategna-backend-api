package com.muyategna.backend.job_request.dto.job_lead;

import com.muyategna.backend.job_request.enums.JobLeadStatus;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobLeadDto {
    private Long id;
    private Long jobRequestId;
    private JobLeadStatus status;
    private Integer maxClaims;
    private Integer claimedCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
