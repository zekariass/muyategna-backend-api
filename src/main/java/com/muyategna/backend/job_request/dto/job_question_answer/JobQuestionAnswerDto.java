package com.muyategna.backend.job_request.dto.job_question_answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobQuestionAnswerDto {
    private Long id;

    private Long jobRequestId;

    private Long flowQuestionId;

    private String answerText;

    private BigDecimal answerNumber;

    private Boolean answerBoolean;

    private LocalDate answerDate;

    private Long[] answerOptionIds;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
