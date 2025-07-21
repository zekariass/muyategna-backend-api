package com.muyategna.backend.job_request.dto.request_media;

import com.muyategna.backend.job_request.entity.JobRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestMediaDto {
    private Long id;
    private JobRequest request;
    private String mediaUrl;
}
