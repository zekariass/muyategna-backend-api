package com.muyategna.backend.job_request.dto.question_translation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobQuestionTranslationMinimalDto {
    private Long id;
    private Long jobQuestionId;
    private Long languageId;
    private String question;
    private String helpText;
}
