package com.muyategna.backend.job_request.dto.job_request;

import com.muyategna.backend.job_request.enums.JobStatus;
import com.muyategna.backend.job_request.enums.WhenToStartJob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestMinimalDto {
    private Long id;
    private Long serviceId;
    private Long customerId;
    private String description;
    private BigDecimal budget;
    private WhenToStartJob whenToStartJob;
    private JobStatus status;
    private Long addressId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
