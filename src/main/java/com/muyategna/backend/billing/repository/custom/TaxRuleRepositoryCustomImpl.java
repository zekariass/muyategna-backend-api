package com.muyategna.backend.billing.repository.custom;

import com.muyategna.backend.billing.entity.QTaxRule;
import com.muyategna.backend.billing.entity.TaxRule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

public class TaxRuleRepositoryCustomImpl implements TaxRuleRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    public TaxRuleRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<TaxRule> findActiveTaxRulesByCountryId(Long countryId) {
        QTaxRule taxRule = QTaxRule.taxRule;

        LocalDateTime now = LocalDateTime.now();

        return jpaQueryFactory
                .selectFrom(taxRule)
                .where(
                        taxRule.country.id.eq(countryId),
                        taxRule.isActive.isTrue(),
                        taxRule.effectiveStartDate.loe(now),
                        taxRule.effectiveEndDate.goe(now)
                )
                .fetch();
    }
}
