package com.muyategna.backend.billing.repository.custom;

import com.muyategna.backend.billing.entity.AddOnPlanDiscountEligibility;
import com.muyategna.backend.billing.entity.QAddOnPlan;
import com.muyategna.backend.billing.entity.QAddOnPlanDiscountEligibility;
import com.muyategna.backend.billing.entity.QDiscountPlan;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class AddOnPlanDiscountEligibilityRepositoryCustomImpl implements AddOnPlanDiscountEligibilityRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public AddOnPlanDiscountEligibilityRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<AddOnPlanDiscountEligibility> findAddOnPlanDiscountEligibilityByIdAndCountryId(Long eligibilityId, Long countryId) {

        QAddOnPlanDiscountEligibility eligibility = QAddOnPlanDiscountEligibility.addOnPlanDiscountEligibility;
        QAddOnPlan addOnPlan = QAddOnPlan.addOnPlan;
        QDiscountPlan discountPlan = QDiscountPlan.discountPlan;

        LocalDateTime now = LocalDateTime.now();

        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(eligibility)
                        .leftJoin(eligibility.addOnPlan, addOnPlan)
                        .leftJoin(eligibility.discountPlan, discountPlan)
                        .where(
                                eligibility.id.eq(eligibilityId),
                                eligibility.isActive.isTrue(),
                                addOnPlan.isActive.isTrue(),
                                addOnPlan.expiresAt.after(now),
                                addOnPlan.country.id.eq(countryId),
                                discountPlan.isActive.isTrue(),
                                discountPlan.expiresAt.after(now),
                                discountPlan.country.id.eq(countryId),
                                discountPlan.startsAt.before(now),
                                discountPlan.country.id.eq(countryId),
                                discountPlan.usageLimit.gt(discountPlan.totalUseCount)
                        )
                        .fetchOne()
        );
    }

    @Override
    public List<AddOnPlanDiscountEligibility> findAllAddOnPlanDiscountEligibilityByAddOnPlanIdAndCountryId(Long addOnPlanId, Long countryId) {
        QAddOnPlanDiscountEligibility eligibility = QAddOnPlanDiscountEligibility.addOnPlanDiscountEligibility;
        QAddOnPlan addOnPlan = QAddOnPlan.addOnPlan;
        QDiscountPlan discountPlan = QDiscountPlan.discountPlan;

        LocalDateTime now = LocalDateTime.now();

        return jpaQueryFactory
                .selectFrom(eligibility)
                .leftJoin(eligibility.addOnPlan, addOnPlan)
                .leftJoin(eligibility.discountPlan, discountPlan)
                .where(
                        eligibility.isActive.isTrue(),
                        addOnPlan.id.eq(addOnPlanId),
                        addOnPlan.country.id.eq(countryId),
                        addOnPlan.isActive.isTrue(),
                        discountPlan.isActive.isTrue(),
                        addOnPlan.isActive.isTrue(),
                        discountPlan.isActive.isTrue(),
                        discountPlan.country.id.eq(countryId)

                )
                .fetch();
    }
}
