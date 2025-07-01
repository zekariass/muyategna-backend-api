package com.muyategna.backend.billing.repository.custom;

import com.muyategna.backend.billing.entity.AddOnPlan;
import com.muyategna.backend.billing.entity.QAddOnPlan;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AddOnPlanRepositoryCustomImpl implements AddOnPlanRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public AddOnPlanRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<AddOnPlan> findAddOnPlanByIdAndCountryId(Long addOnPlanId, Long countryId) {

        QAddOnPlan addOnPlan = QAddOnPlan.addOnPlan;
        LocalDateTime now = LocalDateTime.now();

        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(addOnPlan)
                        .where(
                                addOnPlan.id.eq(addOnPlanId),
                                addOnPlan.country.id.eq(countryId),
                                addOnPlan.isActive.isTrue(),
                                addOnPlan.expiresAt.after(now)
                        )
                        .fetchOne()
        );
    }

    @Override
    public List<AddOnPlan> findAllAddOnPlanByCountryId(Long countryId) {

        QAddOnPlan addOnPlan = QAddOnPlan.addOnPlan;
        LocalDateTime now = LocalDateTime.now();

        return jpaQueryFactory
                .selectFrom(addOnPlan)
                .where(
                        addOnPlan.country.id.eq(countryId),
                        addOnPlan.isActive.isTrue(),
                        addOnPlan.expiresAt.after(now)
                )
                .fetch();
    }
}
