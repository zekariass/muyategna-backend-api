package com.muyategna.backend.job_request.dto.job_request;

import com.muyategna.backend.common.enums.ServiceDeliveryMode;
import com.muyategna.backend.job_request.dto.job_start_time_window.JobStartTimeWindowDto;
import com.muyategna.backend.job_request.dto.request_media.JobRequestMediaMinimalDto;
import com.muyategna.backend.job_request.enums.JobStatus;
import com.muyategna.backend.location.dto.address.AddressFlatDto;
import com.muyategna.backend.professional_service.dto.service.ServiceMinimalLocalizedDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestDetailDto {
    private Long id;
    private ServiceMinimalLocalizedDto localizedService;
    private Long customerId;
    private String description;
    private BigDecimal budget;
    private JobStartTimeWindowDto startTimeWindow;
    private JobStatus status;
    private AddressFlatDto address;
    private List<Map<String, Object>> questionAnswers;
    private Map<String, Object> extraData;
    private List<JobRequestMediaMinimalDto> mediaList;
    private ServiceDeliveryMode deliveryMode;
    private LocalDateTime createdAt;
}
