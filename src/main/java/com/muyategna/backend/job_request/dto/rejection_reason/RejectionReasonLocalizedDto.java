package com.muyategna.backend.job_request.dto.rejection_reason;

import com.muyategna.backend.job_request.enums.RejectionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RejectionReasonLocalizedDto {
    private Long id;
    private String code;
    private Long languageId;
    private String displayTitle;
    private String description;
    private Boolean isActive;
    private RejectionTypeEnum type;
}
