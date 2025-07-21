package com.muyategna.backend.job_request.dto.request_media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestMediaCreateDto {
    private String originalFilename;
    private String newFilename;
    private String extension;
    private String contentType;
    private Long jobRequestId;
    private byte[] content;
}
