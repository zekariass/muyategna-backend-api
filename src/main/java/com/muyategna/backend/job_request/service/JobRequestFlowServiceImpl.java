package com.muyategna.backend.job_request.service;

import com.muyategna.backend.job_request.dto.request_flow.JobRequestFlowPublicDto;
import com.muyategna.backend.job_request.mapper.JobRequestFlowMapper;
import com.muyategna.backend.job_request.repository.JobRequestFlowRepository;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobRequestFlowServiceImpl implements JobRequestFlowService {

    private final JobRequestFlowRepository jobRequestFlowRepository;

    @Autowired
    public JobRequestFlowServiceImpl(JobRequestFlowRepository jobRequestFlowRepository) {
        this.jobRequestFlowRepository = jobRequestFlowRepository;
    }

    @Override
    public JobRequestFlowPublicDto getFlowByIdForPublic(Long flowId) {
        return jobRequestFlowRepository.findById(flowId)
                .map(JobRequestFlowMapper::toPublicDto).orElseThrow(() -> new ResourceNotFoundException("Request question flow not found"));
    }
}
