package com.muyategna.backend.billing.repository.custom;

import com.muyategna.backend.billing.entity.QServiceProviderSubscription;
import com.muyategna.backend.billing.enums.RenewalStatusEnum;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public class ServiceProviderSubscriptionRepositoryCustomImpl implements ServiceProviderSubscriptionRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public ServiceProviderSubscriptionRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }


    @Override
    public boolean hasServiceProviderValidSubscription(Long serviceProviderId, BigDecimal subsCreditsPerLead) {
        QServiceProviderSubscription sps = QServiceProviderSubscription.serviceProviderSubscription;

        BooleanExpression hasEnoughCredits = (sps.balance.goe(subsCreditsPerLead))
                .or(sps.trialPeriodEndsAt.isNotNull().and(sps.trialPeriodEndsAt.after(LocalDateTime.now())));

        BooleanExpression hasValidRenewalStatus = sps.renewalStatus
                .in(RenewalStatusEnum.PENDING, RenewalStatusEnum.SUCCESSFUL);

        return jpaQueryFactory.selectOne()
                .from(sps)
                .where(sps.provider.id.eq(serviceProviderId)
                        .and(hasEnoughCredits)
                        .and(sps.isActive.isTrue())
                        .and(sps.cancelledAt.isNull())
                        .and(hasValidRenewalStatus)
                        .and(sps.startedAt.loe(LocalDateTime.now()))
                        .and(sps.endDate.isNull())) // Ensure the subscription has started and has no end date
                .fetchFirst() != null;
    }
}
