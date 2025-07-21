package com.muyategna.backend.job_request.repository.custom;

import com.muyategna.backend.job_request.entity.JobLeadClaim;

import java.util.Set;

public interface JobLeadClaimRepositoryCustom {
    Set<JobLeadClaim> findServiceProviderLeadsByLeadId(Long jobLeadId);
}
