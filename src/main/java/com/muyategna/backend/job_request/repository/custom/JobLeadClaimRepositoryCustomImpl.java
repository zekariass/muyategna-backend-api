package com.muyategna.backend.job_request.repository.custom;

import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.job_request.entity.QJobLeadClaim;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.HashSet;
import java.util.Set;

public class JobLeadClaimRepositoryCustomImpl implements JobLeadClaimRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public JobLeadClaimRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Set<JobLeadClaim> findServiceProviderLeadsByLeadId(Long jobLeadId) {
        return new HashSet<>(queryFactory
                .selectFrom(QJobLeadClaim.jobLeadClaim)
                .where(QJobLeadClaim.jobLeadClaim.jobLead.id.eq(jobLeadId))
                .fetch());
    }
}
