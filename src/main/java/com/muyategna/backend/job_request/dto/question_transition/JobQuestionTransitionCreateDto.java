package com.muyategna.backend.job_request.dto.question_transition;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobQuestionTransitionCreateDto {
    @NotNull(message = "ID cannot be null")
    private Long flowId;

    @NotNull(message = "Current flow question ID cannot be null")
    private Long fromFlowQuestionId;

    @NotNull(message = "Next flow question ID cannot be null")
    private Long toFlowQuestionId;

    private Long optionId;

    private String conditionExpression;
}