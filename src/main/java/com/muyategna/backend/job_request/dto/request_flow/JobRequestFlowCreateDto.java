package com.muyategna.backend.job_request.dto.request_flow;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestFlowCreateDto {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;
    private boolean isActive;

    @NotBlank(message = "Service ID is required")
    private Long serviceId;
}
