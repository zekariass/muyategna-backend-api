package com.muyategna.backend.job_request.dto.question_translation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobQuestionTranslationDto {
    private Long id;
    private Long jobQuestionId;
    private Long languageId;
    private String question;
    private String helpText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
