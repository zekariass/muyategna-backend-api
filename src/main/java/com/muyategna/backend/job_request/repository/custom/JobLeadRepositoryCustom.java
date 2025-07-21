package com.muyategna.backend.job_request.repository.custom;

import com.muyategna.backend.job_request.entity.JobLead;
import com.muyategna.backend.job_request.enums.JobLeadStatus;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface JobLeadRepositoryCustom {
    Optional<JobLead> findLeadForNewAssignmentById(Long leadId);
}
