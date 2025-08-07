package com.muyategna.backend.job_request.dto.job_request;

import com.muyategna.backend.common.enums.ServiceDeliveryMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestUpdateDto {

    @NotNull(message = "ID is required")
    private Long id;

    @NotNull(message = "Service ID is required")
    private Long serviceId;

    @NotBlank(message = "Title is required")
    private String description;

    private BigDecimal budget;

    @NotNull(message = "Start Time Window required")
    private Long startTimeWindowId;

    private List<Map<String, Object>> questionAnswers;

    private Map<String, Object> extraData;

    private ServiceDeliveryMode deliveryMode;


}
