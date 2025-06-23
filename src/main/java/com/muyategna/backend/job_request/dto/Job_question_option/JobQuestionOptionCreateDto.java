package com.muyategna.backend.job_request.dto.Job_question_option;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobQuestionOptionCreateDto {
    @NotNull(message = "Question ID cannot be null")
    private Long questionId;

    @NotBlank(message = "Value cannot be null")
    private String value;

    @NotNull(message = "Order index cannot be null")
    private Integer orderIndex;
}
