package com.muyategna.backend.job_request.dto.option_translation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobQuestionOptionTranslationDto {
    private Long id;
    private Long optionId;
    private Long languageId;
    private String label;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
