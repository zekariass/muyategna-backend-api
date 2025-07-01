package com.muyategna.backend.billing.repository.custom;

import com.muyategna.backend.billing.entity.DiscountPlan;
import com.muyategna.backend.billing.entity.QDiscountPlan;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

public class DiscountPlanRepositoryCustomImpl implements DiscountPlanRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public DiscountPlanRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<DiscountPlan> findActiveDiscountPlanByIdAndCountryId(Long discountPlanId, Long countryId) {

        QDiscountPlan discountPlan = QDiscountPlan.discountPlan;
        LocalDateTime now = LocalDateTime.now();

        return Optional.ofNullable(jpaQueryFactory.selectFrom(discountPlan)
                .where(
                        discountPlan.id.eq(discountPlanId),
                        discountPlan.country.id.eq(countryId),
                        discountPlan.isActive.isTrue(),
                        discountPlan.expiresAt.after(now),
                        discountPlan.startsAt.before(now),
                        discountPlan.usageLimit.gt(discountPlan.totalUseCount)
                )
                .fetchOne());
    }
}
