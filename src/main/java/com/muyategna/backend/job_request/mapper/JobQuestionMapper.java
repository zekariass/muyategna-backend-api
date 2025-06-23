package com.muyategna.backend.job_request.mapper;

import com.muyategna.backend.job_request.dto.job_question.JobQuestionCreateDto;
import com.muyategna.backend.job_request.dto.job_question.JobQuestionDto;
import com.muyategna.backend.job_request.dto.job_question.JobQuestionMinimalDto;
import com.muyategna.backend.job_request.dto.job_question.JobQuestionUpdateDto;
import com.muyategna.backend.job_request.entity.JobQuestion;

import java.util.List;

public final class JobQuestionMapper {

    /**
     * Converts a JobQuestion entity to a JobQuestionDto.
     *
     * @param jobQuestion the entity to convert
     * @return the converted JobQuestionDto
     */
    public static JobQuestionDto toDto(JobQuestion jobQuestion) {
        if (jobQuestion == null) {
            return null;
        }
        return JobQuestionDto.builder()
                .id(jobQuestion.getId())
                .type(jobQuestion.getType())
                .createdAt(jobQuestion.getCreatedAt())
                .updatedAt(jobQuestion.getUpdatedAt())
                .build();
    }


    public static JobQuestionMinimalDto toMinimalDto(JobQuestion jobQuestion) {
        if (jobQuestion == null) {
            return null;
        }
        return JobQuestionMinimalDto.builder()
                .id(jobQuestion.getId())
                .type(jobQuestion.getType())
                .translations(JobQuestionTranslationMapper.toMinimalDtoList(jobQuestion.getTranslations()))
                .options(JobQuestionOptionMapper.toMinimalDtoList(jobQuestion.getOptions()))
                .build();
    }


    /**
     * Converts a JobQuestionDto to a JobQuestion entity.
     *
     * @param jobQuestionDto the DTO to convert
     * @return the converted JobQuestion entity
     */
    public static JobQuestion toEntity(JobQuestionDto jobQuestionDto) {
        if (jobQuestionDto == null) {
            return null;
        }
        JobQuestion jobQuestion = new JobQuestion();
        jobQuestion.setId(jobQuestionDto.getId());
        jobQuestion.setType(jobQuestionDto.getType());
        jobQuestion.setCreatedAt(jobQuestionDto.getCreatedAt());
        return jobQuestion;
    }


    public static JobQuestion toEntity(JobQuestionCreateDto jobQuestionDto) {
        if (jobQuestionDto == null) {
            return null;
        }
        JobQuestion jobQuestion = new JobQuestion();

        jobQuestion.setType(jobQuestionDto.getType());
        return jobQuestion;
    }


    public static JobQuestion toEntity(JobQuestionUpdateDto jobQuestionDto) {
        if (jobQuestionDto == null) {
            return null;
        }
        JobQuestion jobQuestion = new JobQuestion();
        jobQuestion.setId(jobQuestionDto.getId());
        jobQuestion.setType(jobQuestionDto.getType());
        return jobQuestion;
    }


    public static List<JobQuestionDto> toDtoList(List<JobQuestion> jobQuestions) {
        return jobQuestions.stream().map(JobQuestionMapper::toDto).toList();
    }


}
