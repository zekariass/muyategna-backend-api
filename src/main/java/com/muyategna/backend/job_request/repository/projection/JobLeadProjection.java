package com.muyategna.backend.job_request.repository.projection;

import com.muyategna.backend.job_request.enums.JobLeadStatus;

import java.time.Instant;
import java.time.LocalDateTime;

public interface JobLeadProjection {
    Long getId();
    Long getJobRequestId();
    JobLeadStatus getStatus();
    Integer getMaxClaims();
    Integer getClaimedCount();
    LocalDateTime getCreatedAt();
    Double getDistanceMeters();
}
