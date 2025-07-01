package com.muyategna.backend.billing.repository.custom;

import com.muyategna.backend.billing.entity.QSubscriptionPlan;
import com.muyategna.backend.billing.entity.SubscriptionPlan;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class SubscriptionPlanRepositoryCustomImpl implements SubscriptionPlanRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    public SubscriptionPlanRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<SubscriptionPlan> findSubscriptionPlanByIdAndCountryId(Long subscriptionPlanId, Long countryId) {

        QSubscriptionPlan subscriptionPlan = QSubscriptionPlan.subscriptionPlan;

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(subscriptionPlan)
                .where(
                        subscriptionPlan.id.eq(subscriptionPlanId),
                        subscriptionPlan.country.id.eq(countryId),
                        subscriptionPlan.isActive.isTrue())
                .fetchOne());
    }


    @Override
    public List<SubscriptionPlan> findSubscriptionPlansByCountryId(Long countryId) {
        QSubscriptionPlan subscriptionPlan = QSubscriptionPlan.subscriptionPlan;

        return jpaQueryFactory
                .selectFrom(subscriptionPlan)
                .where(
                        subscriptionPlan.country.id.eq(countryId),
                        subscriptionPlan.isActive.isTrue())
                .fetch();
    }
}
