package com.muyategna.backend.job_request.service.producer;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface JobRequestProducerService {
    void enqueueUploadJobRequestFiles(Long id, List<MultipartFile> files) throws IOException;
}
