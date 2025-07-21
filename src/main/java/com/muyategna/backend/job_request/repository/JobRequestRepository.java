package com.muyategna.backend.job_request.repository;

import com.muyategna.backend.job_request.entity.JobRequest;
import com.muyategna.backend.job_request.repository.custom.JobRequestRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JobRequestRepository extends JpaRepository<JobRequest, Long>, JobRequestRepositoryCustom {

    @Query("""
    SELECT jr FROM JobRequest jr
    JOIN FETCH jr.service
    WHERE jr.id = :jobRequestId
    """)
    Optional<JobRequest> findByIdWithService(@Param("jobRequestId") Long jobRequestId);

    @Query("""
    SELECT jr FROM JobRequest jr
    JOIN FETCH jr.service
    JOIN FETCH jr.address
    JOIN FETCH jr.startTimeWindow
    LEFT JOIN FETCH jr.mediaList
    WHERE jr.id = :jobRequestId
    """)
    Optional<JobRequest> findJobRequestWithRelatedEntities(Long jobRequestId);
}
