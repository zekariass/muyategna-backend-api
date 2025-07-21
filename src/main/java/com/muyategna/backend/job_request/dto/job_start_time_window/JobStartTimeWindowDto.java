package com.muyategna.backend.job_request.dto.job_start_time_window;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobStartTimeWindowDto {
    private Long id;
    private String code;
    private String label;
    private Integer minOffsetDays;
    private Integer maxOffsetDays;
    private Boolean isActive = true;
}
