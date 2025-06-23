package com.muyategna.backend.job_request.dto.request_flow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestFlowDto {
    private Long id;
    private String name;
    private String description;
    private boolean isActive;
    private Long serviceId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
