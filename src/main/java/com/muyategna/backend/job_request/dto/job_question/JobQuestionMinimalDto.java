package com.muyategna.backend.job_request.dto.job_question;

import com.muyategna.backend.job_request.dto.Job_question_option.JobQuestionOptionMinimalDto;
import com.muyategna.backend.job_request.dto.question_translation.JobQuestionTranslationMinimalDto;
import com.muyategna.backend.job_request.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobQuestionMinimalDto {
    private Long id;
    private QuestionType type;
    private List<JobQuestionTranslationMinimalDto> translations;
    private List<JobQuestionOptionMinimalDto> options;
}