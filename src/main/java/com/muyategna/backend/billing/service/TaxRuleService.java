package com.muyategna.backend.billing.service;

import com.muyategna.backend.location.entity.Country;

import java.math.BigDecimal;
import java.util.List;

public interface TaxRuleService {
    BigDecimal computeTaxAmount(BigDecimal amount, Country country);

    List<Long> getTaxRuleIdsByCountry(Country country);
}
