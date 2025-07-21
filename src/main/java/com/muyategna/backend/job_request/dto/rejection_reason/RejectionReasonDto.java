package com.muyategna.backend.job_request.dto.rejection_reason;

import com.muyategna.backend.job_request.enums.RejectionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RejectionReasonDto {
    private Long id;
    private String code;
    private Boolean isActive;
    private RejectionTypeEnum type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
