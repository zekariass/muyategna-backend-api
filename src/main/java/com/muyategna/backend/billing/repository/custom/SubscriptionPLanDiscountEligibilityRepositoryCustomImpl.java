package com.muyategna.backend.billing.repository.custom;

import com.muyategna.backend.billing.entity.QDiscountPlan;
import com.muyategna.backend.billing.entity.QSubscriptionPlan;
import com.muyategna.backend.billing.entity.QSubscriptionPlanDiscountEligibility;
import com.muyategna.backend.billing.entity.SubscriptionPlanDiscountEligibility;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class SubscriptionPLanDiscountEligibilityRepositoryCustomImpl implements SubscriptionPLanDiscountEligibilityRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public SubscriptionPLanDiscountEligibilityRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<SubscriptionPlanDiscountEligibility> findSubscriptionPlanDiscountEligibilityByIdAndCountryId(Long eligibilityId, Long countryId) {
        QSubscriptionPlanDiscountEligibility eligibility = QSubscriptionPlanDiscountEligibility.subscriptionPlanDiscountEligibility;
        QSubscriptionPlan subscriptionPlan = QSubscriptionPlan.subscriptionPlan;
        QDiscountPlan discountPlan = QDiscountPlan.discountPlan;

        LocalDateTime now = LocalDateTime.now();

        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(eligibility)
                        .leftJoin(eligibility.subscriptionPlan, subscriptionPlan)
                        .leftJoin(eligibility.discountPlan, discountPlan)
                        .where(
                                eligibility.id.eq(eligibilityId),
                                eligibility.isActive.isTrue(),
                                subscriptionPlan.isActive.isTrue(),
                                subscriptionPlan.country.id.eq(countryId),
                                discountPlan.isActive.isTrue(),
                                discountPlan.expiresAt.after(now),
                                discountPlan.country.id.eq(countryId)
                        )
                        .fetchOne()
        );

    }

    @Override
    public List<SubscriptionPlanDiscountEligibility> findAllBySubscriptionPlanIdAndByCountryId(Long subscriptionPlanId, Long countryId) {
        QSubscriptionPlanDiscountEligibility eligibility = QSubscriptionPlanDiscountEligibility.subscriptionPlanDiscountEligibility;
        QSubscriptionPlan subscriptionPlan = QSubscriptionPlan.subscriptionPlan;
        QDiscountPlan discountPlan = QDiscountPlan.discountPlan;

        LocalDateTime now = LocalDateTime.now();

        return jpaQueryFactory
                .selectFrom(eligibility)
                .leftJoin(eligibility.subscriptionPlan, subscriptionPlan)
                .leftJoin(eligibility.discountPlan, discountPlan)
                .where(
                        subscriptionPlan.id.eq(subscriptionPlanId),
                        subscriptionPlan.isActive.isTrue(),
                        subscriptionPlan.country.id.eq(countryId),
                        discountPlan.country.id.eq(countryId),
                        discountPlan.expiresAt.after(now),
                        discountPlan.isActive.isTrue()
                )
                .fetch();
    }
}
