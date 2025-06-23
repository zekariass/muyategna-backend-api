package com.muyategna.backend.job_request.mapper;

import com.muyategna.backend.common.entity.Language;
import com.muyategna.backend.job_request.dto.question_translation.JobQuestionTranslationCreateDto;
import com.muyategna.backend.job_request.dto.question_translation.JobQuestionTranslationDto;
import com.muyategna.backend.job_request.dto.question_translation.JobQuestionTranslationMinimalDto;
import com.muyategna.backend.job_request.dto.question_translation.JobQuestionTranslationUpdateDto;
import com.muyategna.backend.job_request.entity.JobQuestion;
import com.muyategna.backend.job_request.entity.JobQuestionTranslation;

import java.util.List;

public final class JobQuestionTranslationMapper {

    public static JobQuestionTranslationDto toDto(JobQuestionTranslation entity) {
        if (entity == null) {
            return null;
        }
        return JobQuestionTranslationDto.builder()
                .id(entity.getId())
                .jobQuestionId(entity.getJobQuestion().getId())
                .languageId(entity.getLanguage().getId())
                .question(entity.getQuestion())
                .helpText(entity.getHelpText())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static JobQuestionTranslation toEntity(JobQuestionTranslationDto dto,
                                                  JobQuestion jobQuestion,
                                                  Language language) {
        if (dto == null) {
            return null;
        }
        JobQuestionTranslation entity = new JobQuestionTranslation();
        entity.setId(dto.getId());
        entity.setJobQuestion(jobQuestion);
        entity.setLanguage(language);
        entity.setQuestion(dto.getQuestion());
        entity.setHelpText(dto.getHelpText());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }


    public static JobQuestionTranslation toEntity(JobQuestionTranslationCreateDto dto,
                                                  JobQuestion jobQuestion,
                                                  Language language) {
        if (dto == null) {
            return null;
        }
        JobQuestionTranslation entity = new JobQuestionTranslation();
        entity.setJobQuestion(jobQuestion);
        entity.setLanguage(language);
        entity.setQuestion(dto.getQuestion());
        entity.setHelpText(dto.getHelpText());
        return entity;
    }


    public static JobQuestionTranslation toEntity(JobQuestionTranslationUpdateDto dto,
                                                  JobQuestion jobQuestion,
                                                  Language language) {
        if (dto == null) {
            return null;
        }
        JobQuestionTranslation entity = new JobQuestionTranslation();
        entity.setId(dto.getId());
        entity.setJobQuestion(jobQuestion);
        entity.setLanguage(language);
        entity.setQuestion(dto.getQuestion());
        entity.setHelpText(dto.getHelpText());
        return entity;
    }

    public static JobQuestionTranslationMinimalDto toMinimalDto(JobQuestionTranslation entity) {
        if (entity == null) {
            return null;
        }
        return JobQuestionTranslationMinimalDto.builder()
                .id(entity.getId())
                .jobQuestionId(entity.getJobQuestion().getId())
                .languageId(entity.getLanguage().getId())
                .question(entity.getQuestion())
                .helpText(entity.getHelpText())
                .build();
    }


    public static List<JobQuestionTranslationDto> toDtoList(List<JobQuestionTranslation> jobQuestionTranslations) {
        return jobQuestionTranslations.stream()
                .map(JobQuestionTranslationMapper::toDto)
                .toList();
    }

    public static List<JobQuestionTranslationMinimalDto> toMinimalDtoList(List<JobQuestionTranslation> translations) {
        return translations.stream()
                .map(JobQuestionTranslationMapper::toMinimalDto)
                .toList();
    }
}
