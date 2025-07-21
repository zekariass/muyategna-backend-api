package com.muyategna.backend.job_request.dto.request_media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestMediaMinimalDto {
    private Long id;
    private Long requestId;
    private String mediaUrl;
}
