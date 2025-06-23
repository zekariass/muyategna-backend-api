package com.muyategna.backend.job_request.repository;

import com.muyategna.backend.job_request.entity.JobQuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobQuestionAnswerRepository extends JpaRepository<JobQuestionAnswer, Long> {
}
