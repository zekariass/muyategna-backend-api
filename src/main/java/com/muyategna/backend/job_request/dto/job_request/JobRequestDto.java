package com.muyategna.backend.job_request.dto.job_request;

import com.muyategna.backend.common.enums.ServiceDeliveryMode;
import com.muyategna.backend.job_request.dto.job_start_time_window.JobStartTimeWindowDto;
import com.muyategna.backend.job_request.enums.JobStatus;
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
public class JobRequestDto {
    private Long id;
    private Long serviceId;
    private Long customerId;
    private String description;
    private BigDecimal budget;
    private JobStartTimeWindowDto startTimeWindow;
    private JobStatus status;
    private Long addressId;
    private List<Map<String, Object>> questionAnswers;
    private Map<String, Object> extraData;
    private ServiceDeliveryMode deliveryMode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
