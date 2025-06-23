package com.muyategna.backend.job_request.dto.job_question;

import com.muyategna.backend.job_request.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobQuestionDto {
    private Long id;
    private QuestionType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}