package com.muyategna.backend.billing.repository.custom;

import com.muyategna.backend.billing.entity.TaxRule;

import java.util.List;

public interface TaxRuleRepositoryCustom {
    List<TaxRule> findActiveTaxRulesByCountryId(Long countryId);
}
