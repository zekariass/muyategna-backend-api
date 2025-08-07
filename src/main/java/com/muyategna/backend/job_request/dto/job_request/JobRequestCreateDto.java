package com.muyategna.backend.job_request.dto.job_request;

import com.muyategna.backend.common.enums.ServiceDeliveryMode;
import com.muyategna.backend.job_request.enums.JobStatus;
import com.muyategna.backend.location.dto.address.AddressCreateDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestCreateDto {
    @NotNull(message = "Service ID is required")
    private Long serviceId;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Title is required")
    private String description;

    private BigDecimal budget;

    @NotNull(message = "Start Time Window is required")
    private Long startTimeWindowId;

    @NotNull(message = "Status is required")
    private JobStatus status;

    @NotNull(message = "Address is required")
    private AddressCreateDto addressCreateDto;

    private List<Map<String, Object>> questionAnswers;

    private Map<String, Object> extraData;

    private List<MultipartFile> files;

    private ServiceDeliveryMode deliveryMode;

}
