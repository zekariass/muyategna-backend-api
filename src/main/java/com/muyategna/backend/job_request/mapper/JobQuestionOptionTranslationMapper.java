package com.muyategna.backend.job_request.mapper;

import com.muyategna.backend.common.entity.Language;
import com.muyategna.backend.job_request.dto.option_translation.JobQuestionOptionTranslationCreateDto;
import com.muyategna.backend.job_request.dto.option_translation.JobQuestionOptionTranslationDto;
import com.muyategna.backend.job_request.dto.option_translation.JobQuestionOptionTranslationMinimalDto;
import com.muyategna.backend.job_request.dto.option_translation.JobQuestionOptionTranslationUpdateDto;
import com.muyategna.backend.job_request.entity.JobQuestionOption;
import com.muyategna.backend.job_request.entity.JobQuestionOptionTranslation;

import java.util.List;
import java.util.stream.Collectors;

public final class JobQuestionOptionTranslationMapper {

    public static JobQuestionOptionTranslationDto toDto(JobQuestionOptionTranslation entity) {
        if (entity == null) {
            return null;
        }
        return JobQuestionOptionTranslationDto.builder()
                .id(entity.getId())
                .optionId(entity.getOption().getId())
                .languageId(entity.getLanguage().getId())
                .label(entity.getLabel())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }


    public static JobQuestionOptionTranslationMinimalDto toMinimalDto(JobQuestionOptionTranslation entity) {
        if (entity == null) {
            return null;
        }
        return JobQuestionOptionTranslationMinimalDto.builder()
                .id(entity.getId())
                .optionId(entity.getOption().getId())
                .languageId(entity.getLanguage().getId())
                .label(entity.getLabel())
                .build();
    }


    public static JobQuestionOptionTranslation toEntity(JobQuestionOptionTranslationDto dto,
                                                        JobQuestionOption option,
                                                        Language language) {
        if (dto == null) {
            return null;
        }
        JobQuestionOptionTranslation entity = new JobQuestionOptionTranslation();
        entity.setId(dto.getId());
        entity.setLabel(dto.getLabel());
        entity.setOption(option);
        entity.setLanguage(language);
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }

    public static JobQuestionOptionTranslation toEntity(JobQuestionOptionTranslationCreateDto dto,
                                                        JobQuestionOption option,
                                                        Language language) {
        if (dto == null) {
            return null;
        }
        JobQuestionOptionTranslation entity = new JobQuestionOptionTranslation();
        entity.setLabel(dto.getLabel());
        entity.setOption(option);
        entity.setLanguage(language);
        return entity;
    }


    public static JobQuestionOptionTranslation toEntity(JobQuestionOptionTranslationUpdateDto dto,
                                                        JobQuestionOption option,
                                                        Language language) {
        if (dto == null) {
            return null;
        }
        JobQuestionOptionTranslation entity = new JobQuestionOptionTranslation();
        entity.setId(dto.getId());
        entity.setLabel(dto.getLabel());
        entity.setOption(option);
        entity.setLanguage(language);
        return entity;
    }


    public static List<JobQuestionOptionTranslationDto> toDtoList(List<JobQuestionOptionTranslation> translations) {
        return translations.stream().map(JobQuestionOptionTranslationMapper::toDto).collect(Collectors.toList());
    }

    public static List<JobQuestionOptionTranslationMinimalDto> toMinimalDtoList(List<JobQuestionOptionTranslation> translations) {
        return translations.stream()
                .map(JobQuestionOptionTranslationMapper::toMinimalDto)
                .collect(Collectors.toList());
    }
}
