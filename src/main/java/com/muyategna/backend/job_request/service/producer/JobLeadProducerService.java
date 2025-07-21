package com.muyategna.backend.job_request.service.producer;

public interface JobLeadProducerService {
    void enqueueJobLeadGenerationForNewJobRequest(Long jobRequestId);
}
