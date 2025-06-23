package com.muyategna.backend.job_request.repository;

import com.muyategna.backend.job_request.entity.JobRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRequestRepository extends JpaRepository<JobRequest, Long> {

}
