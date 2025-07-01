package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.add_on_plan_discount_eligibility.AddOnPlanDiscountEligibilityDto;
import com.muyategna.backend.billing.entity.*;
import com.muyategna.backend.billing.mapper.AddOnPlanDiscountEligibilityMapper;
import com.muyategna.backend.billing.repository.AddOnPlanDiscountEligibilityRepository;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AddOnPlanDiscountEligibilityServiceImpl implements AddOnPlanDiscountEligibilityService {

    private final AddOnPlanDiscountEligibilityRepository repository;
    private final DiscountPlanTranslationService discountPlanTranslationService;
    private final AddOnPlanTranslationService addOnPlanTranslationService;

    @Autowired
    public AddOnPlanDiscountEligibilityServiceImpl(AddOnPlanDiscountEligibilityRepository repository,
                                                   DiscountPlanTranslationService discountPlanTranslationService,
                                                   AddOnPlanTranslationService addOnPlanTranslationService) {
        this.repository = repository;
        this.discountPlanTranslationService = discountPlanTranslationService;
        this.addOnPlanTranslationService = addOnPlanTranslationService;
    }


    @Override
    public AddOnPlanDiscountEligibilityDto getAddOnPlanDiscountEligibilityById(Long eligibilityId) {
        CountryDto countryDto = CountryContextHolder.getCountry();
        AddOnPlanDiscountEligibility eligibility = repository.findAddOnPlanDiscountEligibilityByIdAndCountryId(eligibilityId, countryDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Discount for add-on plan not found"));

        return getAddOnPlanDiscountEligibilityDto(eligibility);
    }

    private AddOnPlanDiscountEligibilityDto getAddOnPlanDiscountEligibilityDto(AddOnPlanDiscountEligibility eligibility) {
        DiscountPlan discountPlan = eligibility.getDiscountPlan();
        AddOnPlan addOnPlan = eligibility.getAddOnPlan();

        DiscountPlanTranslation discountPlanTranslation =
                discountPlanTranslationService
                        .getDiscountPlanTranslationByDiscountPlanId(discountPlan.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Discount plan translation not found with id: " + discountPlan.getId() + ". Please check the database."));

        AddOnPlanTranslation addOnPlanTranslation =
                addOnPlanTranslationService
                        .getAddOnPlanTranslationByAddOnPlanId(addOnPlan.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Add-on plan translation not found with id: " + addOnPlan.getId() + ". Please check the database."));

        return AddOnPlanDiscountEligibilityMapper.toDto(eligibility, addOnPlanTranslation, discountPlanTranslation);
    }


    @Override
    public List<AddOnPlanDiscountEligibilityDto> getAllAddOnPlanDiscountEligibilityByAddOnPlanId(Long addOnPlanId) {

        CountryDto countryDto = CountryContextHolder.getCountry();
        List<AddOnPlanDiscountEligibility> eligibilityList = repository.findAllAddOnPlanDiscountEligibilityByAddOnPlanIdAndCountryId(addOnPlanId, countryDto.getId());

        List<AddOnPlanDiscountEligibilityDto> eligibilityDtoList = new ArrayList<>();
        for (AddOnPlanDiscountEligibility eligibility : eligibilityList) {
            eligibilityDtoList.add(getAddOnPlanDiscountEligibilityDto(eligibility));
        }

        return eligibilityDtoList;
    }
}
