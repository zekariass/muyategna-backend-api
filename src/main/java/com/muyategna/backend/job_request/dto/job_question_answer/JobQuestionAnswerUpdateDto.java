package com.muyategna.backend.job_request.dto.job_question_answer;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobQuestionAnswerUpdateDto {

    @NotNull(message = "Job request ID is required")
    private Long id;

    @NotNull(message = "Job request is required")
    private Long jobRequestId;

    @NotNull(message = "Flow question is required")
    private Long flowQuestionId;

    private String answerText;

    private BigDecimal answerNumber;

    private Boolean answerBoolean;

    private LocalDate answerDate;

    private Long[] answerOptionIds;
}
