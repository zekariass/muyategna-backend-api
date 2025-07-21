package com.muyategna.backend.job_request.repository.custom;

import com.muyategna.backend.job_request.entity.JobLead;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class JobLeadRepositoryCustomImpl implements JobLeadRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public JobLeadRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<JobLead> findLeadForNewAssignmentById(Long leadId) {
        return Optional.empty();


    }
}
