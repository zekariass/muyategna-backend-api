package com.muyategna.backend.job_request.mapper;

import com.muyategna.backend.job_request.dto.flow_question.JobRequestFlowQuestionCreateDto;
import com.muyategna.backend.job_request.dto.flow_question.JobRequestFlowQuestionDto;
import com.muyategna.backend.job_request.dto.flow_question.JobRequestFlowQuestionMinimalDto;
import com.muyategna.backend.job_request.entity.JobQuestion;
import com.muyategna.backend.job_request.entity.JobRequestFlow;
import com.muyategna.backend.job_request.entity.JobRequestFlowQuestion;

import java.util.List;

public final class JobRequestFlowQuestionMapper {

    public static JobRequestFlowQuestionDto toDto(JobRequestFlowQuestion entity) {
        if (entity == null) {
            return null;
        }
        return JobRequestFlowQuestionDto.builder()
                .id(entity.getId())
                .flowId(entity.getFlow().getId())
                .questionId(entity.getQuestion().getId())
                .orderIndex(entity.getOrderIndex())
                .isStart(entity.isStart())
                .isTerminal(entity.isTerminal())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }


    public static JobRequestFlowQuestionMinimalDto toMinimalDto(JobRequestFlowQuestion entity) {
        if (entity == null) {
            return null;
        }
        return JobRequestFlowQuestionMinimalDto.builder()
                .id(entity.getId())
                .flowId(entity.getFlow().getId())
                .question(JobQuestionMapper.toMinimalDto(entity.getQuestion()))
                .orderIndex(entity.getOrderIndex())
                .isStart(entity.isStart())
                .isTerminal(entity.isTerminal())
                .build();
    }


    public static JobRequestFlowQuestion toEntity(JobRequestFlowQuestionDto dto,
                                                  JobRequestFlow flow,
                                                  JobQuestion question) {
        if (dto == null || flow == null) {
            return null;
        }
        JobRequestFlowQuestion entity = new JobRequestFlowQuestion();
        entity.setId(dto.getId());
        entity.setFlow(flow);
        entity.setQuestion(question);
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setStart(dto.isStart());
        entity.setTerminal(dto.isTerminal());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }

    public static JobRequestFlowQuestion toEntity(JobRequestFlowQuestionCreateDto dto,
                                                  JobRequestFlow flow,
                                                  JobQuestion question) {
        if (dto == null || flow == null) {
            return null;
        }
        JobRequestFlowQuestion entity = new JobRequestFlowQuestion();
        entity.setFlow(flow);
        entity.setQuestion(question);
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setStart(dto.getIsStart());
        entity.setTerminal(dto.getIsTerminal());
        return entity;
    }


    public static List<JobRequestFlowQuestionDto> toDtoList(List<JobRequestFlowQuestion> entities) {
        return entities.stream()
                .map(JobRequestFlowQuestionMapper::toDto)
                .toList();
    }


    public static List<JobRequestFlowQuestionMinimalDto> toMinimalDtoList(List<JobRequestFlowQuestion> entities) {
        return entities.stream()
                .map(JobRequestFlowQuestionMapper::toMinimalDto)
                .toList();
    }
}
