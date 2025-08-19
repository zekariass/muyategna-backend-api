package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.TaxRule;
import com.muyategna.backend.billing.repository.custom.TaxRuleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRuleRepository extends JpaRepository<TaxRule, Long>, TaxRuleRepositoryCustom {
}
