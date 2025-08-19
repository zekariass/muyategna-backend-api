package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.TaxRule;
import com.muyategna.backend.billing.repository.TaxRuleRepository;
import com.muyategna.backend.location.entity.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Slf4j
public class TaxRuleServiceImpl implements TaxRuleService {

    private final TaxRuleRepository taxRuleRepository;

    @Autowired
    public TaxRuleServiceImpl(TaxRuleRepository taxRuleRepository) {
        this.taxRuleRepository = taxRuleRepository;
    }

//    @Override
//    public BigDecimal computeTaxAmount(BigDecimal amount, Country country) {
//        List<TaxRule> taxRules = taxRuleRepository.findActiveTaxRulesByCountryId(country.getId());
//        if (taxRules.isEmpty()) {
//            return BigDecimal.ZERO;
//        }
//
//        BigDecimal totalTax = BigDecimal.ZERO;
//
//        for (TaxRule taxRule : taxRules) {
//            BigDecimal ruleTax = BigDecimal.ZERO;
//
//            if (taxRule.getPercentageValue() != null) {
//                BigDecimal taxPercentage = taxRule.getPercentageValue();
//                BigDecimal percentageAmount = amount
//                        .multiply(taxPercentage)
//                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
//                ruleTax = ruleTax.add(percentageAmount);
//            }
//
//            if (taxRule.getFixedValue() != null) {
//                ruleTax = ruleTax.add(taxRule.getFixedValue());
//            }
//
//            totalTax = totalTax.add(ruleTax);
//        }
//
//        return totalTax.setScale(2, RoundingMode.HALF_UP); // round final tax amount to 2 decimals
//    }
//
//
//    @Override
//    public List<Long> getTaxRuleIdsByCountry(Country country) {
//        List<TaxRule> taxRules = taxRuleRepository.findActiveTaxRulesByCountryId(country.getId());
//        return taxRules.stream()
//                .map(TaxRule::getId)
//                .toList();
//    }


    @Override
    public BigDecimal computeTaxAmount(BigDecimal amount, Country country) {
        List<TaxRule> taxRules = taxRuleRepository.findActiveTaxRulesByCountryId(country.getId());
        if (taxRules.isEmpty() || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalTax = taxRules.stream()
                .map(taxRule -> computeRuleTax(amount, taxRule))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalTax.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal computeRuleTax(BigDecimal amount, TaxRule taxRule) {
        BigDecimal ruleTax = BigDecimal.ZERO;

        if (taxRule.getPercentageValue() != null) {
            BigDecimal percentageAmount = amount
                    .multiply(taxRule.getPercentageValue())
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            ruleTax = ruleTax.add(percentageAmount);
        }

        if (taxRule.getFixedValue() != null) {
            ruleTax = ruleTax.add(taxRule.getFixedValue());
        }

        return ruleTax;
    }

    @Override
    public List<Long> getTaxRuleIdsByCountry(Country country) {
        return taxRuleRepository.findActiveTaxRulesByCountryId(country.getId()).stream()
                .map(TaxRule::getId)
                .toList();
    }

}
