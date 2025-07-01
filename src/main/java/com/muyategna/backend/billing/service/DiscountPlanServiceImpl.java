package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.discount_plan.DiscountPlanLocalizedDto;
import com.muyategna.backend.billing.entity.DiscountPlan;
import com.muyategna.backend.billing.entity.DiscountPlanTranslation;
import com.muyategna.backend.billing.mapper.DiscountPlanMapper;
import com.muyategna.backend.billing.repository.DiscountPlanRepository;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DiscountPlanServiceImpl implements DiscountPlanService {

    private final DiscountPlanRepository repository;
    private final DiscountPlanTranslationService discountPlanTranslationService;

    @Autowired
    public DiscountPlanServiceImpl(DiscountPlanRepository repository, DiscountPlanTranslationService discountPlanTranslationService) {
        this.repository = repository;
        this.discountPlanTranslationService = discountPlanTranslationService;
    }

    @Override
    public DiscountPlanLocalizedDto getDiscountPlanById(Long discountPlanId) {
        log.info("Retrieving discount plan with id: {}", discountPlanId);
        CountryDto countryDto = CountryContextHolder.getCountry();
        DiscountPlan discountPlan = repository.findActiveDiscountPlanByIdAndCountryId(discountPlanId, countryDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Active discount plan not found with id: " + discountPlanId + ". Please check the database."));

        DiscountPlanTranslation translation = discountPlanTranslationService.getDiscountPlanTranslationByDiscountPlanId(discountPlan.getId()).orElseThrow(() -> new ResourceNotFoundException("Discount plan translation not found with id: " + discountPlan.getId() + ". Please check the database."));
        log.info("Retrieved discount plan with id: {}", discountPlanId);
        return DiscountPlanMapper.toLocalizedDto(discountPlan, translation);
    }
}
