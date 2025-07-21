package com.muyategna.backend.job_request.dto.job_request;

import com.muyategna.backend.job_request.dto.job_start_time_window.JobStartTimeWindowDto;
import com.muyategna.backend.job_request.enums.JobStatus;
import com.muyategna.backend.location.dto.address.AddressFlatDto;
import com.muyategna.backend.professional_service.dto.service.ServiceMinimalLocalizedDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestListDto {
    private Long id;
    private ServiceMinimalLocalizedDto service;
    private String description;
    private BigDecimal budget;
    private JobStartTimeWindowDto startTimeWindow;
    private JobStatus status;
    private AddressFlatDto address;
}
