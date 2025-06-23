package com.muyategna.backend.job_request.service;

import com.muyategna.backend.job_request.dto.request_flow.JobRequestFlowPublicDto;

public interface JobRequestFlowService {
    public JobRequestFlowPublicDto getFlowByIdForPublic(Long flowId);
}
