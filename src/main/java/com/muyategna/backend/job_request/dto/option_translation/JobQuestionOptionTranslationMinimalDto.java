package com.muyategna.backend.job_request.dto.option_translation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobQuestionOptionTranslationMinimalDto {
    private Long id;
    private Long optionId;
    private Long languageId;
    private String label;
}
