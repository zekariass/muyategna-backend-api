package com.muyategna.backend.job_request.dto.job_request;

import com.muyategna.backend.job_request.dto.job_question_answer.JobQuestionAnswerCreateDto;
import com.muyategna.backend.job_request.enums.JobStatus;
import com.muyategna.backend.job_request.enums.WhenToStartJob;
import com.muyategna.backend.location.dto.address.AddressCreateDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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

    @NotNull(message = "When to start is required")
    private WhenToStartJob whenToStartJob;

    @NotNull(message = "Status is required")
    private JobStatus status;

    @NotNull(message = "Address ID is required")
    private AddressCreateDto addressCreateDto;

    private List<JobQuestionAnswerCreateDto> answers;

}
