package com.muyategna.backend.job_request.mapper;

import com.muyategna.backend.job_request.dto.rejection_reason.RejectionReasonDto;
import com.muyategna.backend.job_request.dto.rejection_reason.RejectionReasonLocalizedDto;
import com.muyategna.backend.job_request.entity.RejectionReason;
import com.muyategna.backend.job_request.entity.RejectionReasonTranslation;

public final class RejectionReasonMapper {
    public static RejectionReasonLocalizedDto toLocalizedDto(RejectionReason entity,
                                                             RejectionReasonTranslation translation) {
        if (entity == null) {
            return null;
        }
        return RejectionReasonLocalizedDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .isActive(entity.getIsActive())
                .type(entity.getType())
                .displayTitle(translation.getDisplayTitle())
                .description(translation.getDescription())
                .languageId(translation.getLanguage().getId())
                .build();
    }


    public static RejectionReasonDto toDto(RejectionReason entity) {
        if (entity == null) {
            return null;
        }
        return RejectionReasonDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .isActive(entity.getIsActive())
                .type(entity.getType())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }


    public static RejectionReason toEntity(RejectionReasonDto dto) {
        if (dto == null) {
            return null;
        }
        RejectionReason entity = new RejectionReason();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setIsActive(dto.getIsActive());
        entity.setType(dto.getType());
        return entity;
    }
}
