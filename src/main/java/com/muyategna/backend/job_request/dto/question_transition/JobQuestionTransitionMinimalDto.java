package com.muyategna.backend.job_request.dto.question_transition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobQuestionTransitionMinimalDto {
    private Long id;
    private Long flowId;
    private Long fromFlowQuestionId;
    private Long toFlowQuestionId;
    private Long optionId;
    private String conditionExpression;
}