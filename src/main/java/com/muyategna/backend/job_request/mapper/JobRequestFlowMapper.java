package com.muyategna.backend.job_request.mapper;

import com.muyategna.backend.job_request.dto.request_flow.JobRequestFlowCreateDto;
import com.muyategna.backend.job_request.dto.request_flow.JobRequestFlowDto;
import com.muyategna.backend.job_request.dto.request_flow.JobRequestFlowPublicDto;
import com.muyategna.backend.job_request.dto.request_flow.JobRequestFlowUpdateDto;
import com.muyategna.backend.job_request.entity.JobRequestFlow;
import com.muyategna.backend.professional_service.entity.Service;

import java.util.List;

public final class JobRequestFlowMapper {

    public static JobRequestFlowDto toDto(JobRequestFlow entity) {
        if (entity == null) {
            return null;
        }
        return JobRequestFlowDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .isActive(entity.isActive())
                .serviceId(entity.getService().getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }


    public static JobRequestFlowPublicDto toPublicDto(JobRequestFlow entity) {
        if (entity == null) {
            return null;
        }
        return JobRequestFlowPublicDto.builder()
                .id(entity.getId())
                .serviceId(entity.getService().getId())
                .flowQuestions(JobRequestFlowQuestionMapper.toMinimalDtoList(entity.getQuestions()))
                .transitions(JobQuestionTransitionMapper.toMinimalDtoList(entity.getTransitions()))
                .build();
    }

    public static JobRequestFlow toEntity(JobRequestFlowDto dto,
                                          Service service) {
        if (dto == null) {
            return null;
        }
        JobRequestFlow entity = new JobRequestFlow();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.isActive());
        entity.setService(service);
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }


    public static JobRequestFlow toEntity(JobRequestFlowCreateDto dto,
                                          Service service) {
        if (dto == null) {
            return null;
        }
        JobRequestFlow entity = new JobRequestFlow();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.isActive());
        entity.setService(service);
        return entity;
    }


    public static JobRequestFlow toEntity(JobRequestFlowUpdateDto dto,
                                          Service service) {
        if (dto == null) {
            return null;
        }
        JobRequestFlow entity = new JobRequestFlow();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.isActive());
        entity.setService(service);
        return entity;
    }


    public static List<JobRequestFlowDto> toDto(List<JobRequestFlow> entities) {
        return entities.stream()
                .map(JobRequestFlowMapper::toDto)
                .toList();
    }
}
