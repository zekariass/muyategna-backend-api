package com.muyategna.backend.job_request.dto.Job_question_option;

import com.muyategna.backend.job_request.dto.option_translation.JobQuestionOptionTranslationMinimalDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobQuestionOptionMinimalDto {
    private Long id;
    private Long questionId;
    private String value;
    private Integer orderIndex;
    private List<JobQuestionOptionTranslationMinimalDto> translations;
}
