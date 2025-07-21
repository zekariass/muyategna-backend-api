package com.muyategna.backend.job_request.mapper;

import com.muyategna.backend.job_request.dto.job_start_time_window.JobStartTimeWindowDto;
import com.muyategna.backend.job_request.entity.JobStartTimeWindow;

public final class JobStartTimeWindowMapper {
    public static JobStartTimeWindowDto toDto(com.muyategna.backend.job_request.entity.JobStartTimeWindow entity) {    //NOSONAR
        if (entity == null) {
            return null;
        }
        return JobStartTimeWindowDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .label(entity.getLabel())
                .minOffsetDays(entity.getMinOffsetDays())
                .maxOffsetDays(entity.getMaxOffsetDays())
                .isActive(entity.getIsActive())
                .build();
    }


    public static JobStartTimeWindow toEntity(JobStartTimeWindowDto dto) {
        if (dto == null) {
            return null;
        }
        JobStartTimeWindow entity = new JobStartTimeWindow();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setLabel(dto.getLabel());
        entity.setMinOffsetDays(dto.getMinOffsetDays());
        entity.setMaxOffsetDays(dto.getMaxOffsetDays());
        entity.setIsActive(dto.getIsActive());
        return entity;
    }
}
