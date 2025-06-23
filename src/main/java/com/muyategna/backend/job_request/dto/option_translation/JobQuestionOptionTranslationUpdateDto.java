package com.muyategna.backend.job_request.dto.option_translation;

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
public class JobQuestionOptionTranslationUpdateDto {
    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "Option ID cannot be null")
    private Long optionId;

    @NotNull(message = "Language ID cannot be null")
    private Long languageId;

    @NotBlank(message = "Label is required")
    private String label;
}
