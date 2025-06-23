package com.muyategna.backend.job_request.dto.question_translation;

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
public class JobQuestionTranslationCreateDto {
    @NotNull(message = "Job question ID cannot be null")
    private Long jobQuestionId;

    @NotNull(message = "Language ID cannot be null")
    private Long languageId;

    @NotBlank(message = "Question is required")
    private String question;
    private String helpText;
}
