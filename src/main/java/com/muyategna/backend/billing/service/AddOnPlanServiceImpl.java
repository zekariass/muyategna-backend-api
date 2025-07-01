package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.add_on_plan.AddOnPlanLocalizedDto;
import com.muyategna.backend.billing.entity.AddOnPlan;
import com.muyategna.backend.billing.entity.AddOnPlanTranslation;
import com.muyategna.backend.billing.mapper.AddOnPlanMapper;
import com.muyategna.backend.billing.repository.AddOnPlanRepository;
import com.muyategna.backend.common.dto.language.LanguageDto;
import com.muyategna.backend.common.service.LanguageService;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.system.context.LanguageContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AddOnPlanServiceImpl implements AddOnPlanService {

    private final AddOnPlanRepository addOnPlanRepository;
    private final AddOnPlanTranslationService addOnPlanTranslationService;
    private final LanguageService languageService;

    @Autowired
    public AddOnPlanServiceImpl(AddOnPlanRepository addOnPlanRepository, AddOnPlanTranslationService addOnPlanTranslationService, LanguageService languageService) {
        this.addOnPlanRepository = addOnPlanRepository;
        this.addOnPlanTranslationService = addOnPlanTranslationService;
        this.languageService = languageService;
    }

    @Override
    public AddOnPlanLocalizedDto getAddOnPlanById(Long planId) {
//        Specification<AddOnPlan> addOnPlanSpecification = AddOnPlanSpecification.ofId(planId)
//                .and(AddOnPlanSpecification.isActive(true))
//                .and(AddOnPlanSpecification.isNotExpired())
//                .and(AddOnPlanSpecification.forCountry(CountryContextHolder.getCountry().getId()));


        CountryDto countryDto = CountryContextHolder.getCountry();
        AddOnPlan addOnPlan = addOnPlanRepository.findAddOnPlanByIdAndCountryId(planId, countryDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Add-on plan not found with id: " + planId + ". Please check the database."));

        if (addOnPlan != null) {
            return getLocalizedAddOnPlan(List.of(addOnPlan)).stream().findFirst().orElseThrow(() -> new ResourceNotFoundException("Add-on plan translation not found"));
        }
        throw new ResourceNotFoundException("Add-on plan not found");
    }

    @Override
    public List<AddOnPlanLocalizedDto> getAddOnPlansByCountryId() {
//        Specification<AddOnPlan> addOnPlanSpecification = AddOnPlanSpecification.forService(serviceId)
//                .and(AddOnPlanSpecification.isActive(true))
//                .and(AddOnPlanSpecification.isNotExpired())
//                .and(AddOnPlanSpecification.forCountry(CountryContextHolder.getCountry().getId()));

        CountryDto countryDto = CountryContextHolder.getCountry();
        List<AddOnPlan> addOnPlans = addOnPlanRepository.findAllAddOnPlanByCountryId(countryDto.getId());

        return getLocalizedAddOnPlan(addOnPlans);
    }

    @NotNull
    private List<AddOnPlanLocalizedDto> getLocalizedAddOnPlan(List<AddOnPlan> addOnPlans) {

        List<Long> addOnPlanIds = addOnPlans.stream().map(AddOnPlan::getId).toList();

        List<AddOnPlanTranslation> addOnPlanTranslations = addOnPlanTranslationService.findTranslationsByAddOnPlanIdInAndLanguageId(addOnPlanIds, LanguageContextHolder.getLanguage().getId());

        Map<Long, AddOnPlanTranslation> addOnPlanTranslationMap = addOnPlanTranslations.stream()
                .collect(Collectors.toMap(planTranslation -> planTranslation.getAddOnPlan().getId(), Function.identity()));

        // Fallback to default language
        List<Long> fallbackAddOnPlanIds = new ArrayList<>();
        addOnPlans.forEach(addOnPlan -> {
            AddOnPlanTranslation addOnPlanTranslation = addOnPlanTranslationMap.get(addOnPlan.getId());
            if (addOnPlanTranslation != null) {
                fallbackAddOnPlanIds.add(addOnPlan.getId());
            }
        });

        if (!fallbackAddOnPlanIds.isEmpty()) {

            LanguageDto globalLanguage = languageService.getGlobalLanguage();
            List<AddOnPlanTranslation> fallbackAddOnPlanTranslations = addOnPlanTranslationService.findTranslationsByAddOnPlanIdInAndLanguageId(fallbackAddOnPlanIds, globalLanguage.getId());

            addOnPlanTranslations = new ArrayList<>(addOnPlanTranslations.stream().filter(Objects::nonNull).toList());
            addOnPlanTranslations.addAll(fallbackAddOnPlanTranslations);
        }
        return AddOnPlanMapper.toLocalizedDtoList(addOnPlans, addOnPlanTranslations);

    }
}
