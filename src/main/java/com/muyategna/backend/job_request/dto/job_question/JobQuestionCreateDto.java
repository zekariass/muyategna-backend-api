package com.muyategna.backend.job_request.dto.job_question;

import com.muyategna.backend.job_request.enums.QuestionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobQuestionCreateDto {
    @NotNull(message = "Type is required")
    private QuestionType type;
}