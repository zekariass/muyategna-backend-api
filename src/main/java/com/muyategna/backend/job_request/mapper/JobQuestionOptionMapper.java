package com.muyategna.backend.job_request.mapper;

import com.muyategna.backend.job_request.dto.Job_question_option.JobQuestionOptionCreateDto;
import com.muyategna.backend.job_request.dto.Job_question_option.JobQuestionOptionDto;
import com.muyategna.backend.job_request.dto.Job_question_option.JobQuestionOptionMinimalDto;
import com.muyategna.backend.job_request.dto.Job_question_option.JobQuestionOptionUpdateDto;
import com.muyategna.backend.job_request.entity.JobQuestion;
import com.muyategna.backend.job_request.entity.JobQuestionOption;

import java.util.List;
import java.util.stream.Collectors;

public final class JobQuestionOptionMapper {

    public static JobQuestionOptionDto toDto(JobQuestionOption entity) {
        if (entity == null) {
            return null;
        }
        JobQuestionOptionDto dto = new JobQuestionOptionDto();
        dto.setId(entity.getId());
        dto.setQuestionId(entity.getQuestion().getId());
        dto.setValue(entity.getValue());
        dto.setOrderIndex(entity.getOrderIndex());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }


    public static JobQuestionOptionMinimalDto toMinimalDto(JobQuestionOption entity) {
        if (entity == null) {
            return null;
        }
        JobQuestionOptionMinimalDto dto = new JobQuestionOptionMinimalDto();
        dto.setId(entity.getId());
        dto.setQuestionId(entity.getQuestion().getId());
        dto.setValue(entity.getValue());
        dto.setOrderIndex(entity.getOrderIndex());
        dto.setTranslations(JobQuestionOptionTranslationMapper.toMinimalDtoList(entity.getTranslations()));
        return dto;
    }

    public static JobQuestionOption toEntity(JobQuestionOptionDto dto, JobQuestion question) {
        JobQuestionOption entity = new JobQuestionOption();
        entity.setId(dto.getId());
        entity.setQuestion(question);
        entity.setValue(dto.getValue());
        entity.setOrderIndex(dto.getOrderIndex());
        return entity;
    }

    public static JobQuestionOption toEntity(JobQuestionOptionCreateDto dto,
                                             JobQuestion question) {
        JobQuestionOption entity = new JobQuestionOption();
        entity.setValue(dto.getValue());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setQuestion(question);
        return entity;
    }


    public static JobQuestionOption toEntity(JobQuestionOptionUpdateDto dto,
                                             JobQuestion question) {
        if (dto == null) {
            return null;
        }
        JobQuestionOption entity = new JobQuestionOption();
        entity.setId(dto.getId());
        entity.setValue(dto.getValue());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setQuestion(question);
        return entity;
    }

    public static List<JobQuestionOptionDto> toDtoList(List<JobQuestionOption> entities) {
        return entities.stream().map(JobQuestionOptionMapper::toDto).collect(Collectors.toList());
    }

    public static List<JobQuestionOptionMinimalDto> toMinimalDtoList(List<JobQuestionOption> options) {
        return options.stream()
                .map(JobQuestionOptionMapper::toMinimalDto)
                .collect(Collectors.toList());
    }
}
