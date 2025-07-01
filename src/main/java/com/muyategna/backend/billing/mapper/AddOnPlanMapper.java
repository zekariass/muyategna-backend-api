package com.muyategna.backend.billing.mapper;

import com.muyategna.backend.billing.dto.add_on_plan.AddOnPlanCreateDto;
import com.muyategna.backend.billing.dto.add_on_plan.AddOnPlanDto;
import com.muyategna.backend.billing.dto.add_on_plan.AddOnPlanLocalizedDto;
import com.muyategna.backend.billing.dto.add_on_plan.AddOnPlanUpdateDto;
import com.muyategna.backend.billing.entity.AddOnPlan;
import com.muyategna.backend.billing.entity.AddOnPlanTranslation;
import com.muyategna.backend.location.entity.Country;

import java.util.List;

public final class AddOnPlanMapper {

    public static AddOnPlanDto toDto(AddOnPlan addOnPlan) {
        return AddOnPlanDto.builder()
                .id(addOnPlan.getId())
                .countryId(addOnPlan.getCountry().getId())
                .name(addOnPlan.getName())
                .priceAmount(addOnPlan.getPriceAmount())
                .creditsIncluded(addOnPlan.getCreditsIncluded())
                .sortOrder(addOnPlan.getSortOrder())
                .expiresAt(addOnPlan.getExpiresAt())
                .isDefault(addOnPlan.getIsDefault())
                .isActive(addOnPlan.getIsActive())
                .createdAt(addOnPlan.getCreatedAt())
                .updatedAt(addOnPlan.getUpdatedAt())
                .build();
    }


    public static AddOnPlanLocalizedDto toLocalizedDto(AddOnPlan addOnPlan,
                                                       AddOnPlanTranslation translation) {
        return AddOnPlanLocalizedDto.builder()
                .id(addOnPlan.getId())
                .countryId(addOnPlan.getCountry().getId())
                .name(translation.getDisplayName())
                .description(translation.getDescription())
                .priceAmount(addOnPlan.getPriceAmount())
                .creditsIncluded(addOnPlan.getCreditsIncluded())
                .sortOrder(addOnPlan.getSortOrder())
//                .eligibleDiscountPlan(DiscountPlanMapper.toLocalizedDto(addOnPlan.get()))
                .build();

    }


    public static List<AddOnPlanLocalizedDto> toLocalizedDtoList(List<AddOnPlan> addOnPlans, List<AddOnPlanTranslation> translations) {
        return addOnPlans.stream()
                .map(addOnPlan -> toLocalizedDto(addOnPlan, translations.stream()
                        .filter(translation -> translation.getAddOnPlan().getId().equals(addOnPlan.getId())).findFirst().orElse(new AddOnPlanTranslation())))
                .toList();
    }


    public static AddOnPlan toEntity(AddOnPlanDto addOnPlanDto,
                                     Country country) {
        if (addOnPlanDto == null) {
            return null;
        }
        AddOnPlan addOnPlan = new AddOnPlan();
        addOnPlan.setId(addOnPlanDto.getId());
        addOnPlan.setName(addOnPlanDto.getName());
        addOnPlan.setCountry(country);
        addOnPlan.setPriceAmount(addOnPlanDto.getPriceAmount());
        addOnPlan.setCreditsIncluded(addOnPlanDto.getCreditsIncluded());
        addOnPlan.setSortOrder(addOnPlanDto.getSortOrder());
        addOnPlan.setExpiresAt(addOnPlanDto.getExpiresAt());
        addOnPlan.setIsDefault(addOnPlanDto.getIsDefault());
        addOnPlan.setIsActive(addOnPlanDto.getIsActive());
        return addOnPlan;
    }


    public static AddOnPlan toEntity(AddOnPlanCreateDto addOnPlanDto,
                                     Country country) {
        if (addOnPlanDto == null) {
            return null;
        }
        AddOnPlan addOnPlan = new AddOnPlan();
        addOnPlan.setName(addOnPlanDto.getName());
        addOnPlan.setCountry(country);
        addOnPlan.setPriceAmount(addOnPlanDto.getPriceAmount());
        addOnPlan.setCreditsIncluded(addOnPlanDto.getCreditsIncluded());
        addOnPlan.setSortOrder(addOnPlanDto.getSortOrder());
        addOnPlan.setExpiresAt(addOnPlanDto.getExpiresAt());
        addOnPlan.setIsDefault(addOnPlanDto.getIsDefault());
        addOnPlan.setIsActive(addOnPlanDto.getIsActive());
        return addOnPlan;
    }


    public static AddOnPlan toEntity(AddOnPlanUpdateDto addOnPlanDto,
                                     Country country) {
        if (addOnPlanDto == null) {
            return null;
        }
        AddOnPlan addOnPlan = new AddOnPlan();
        addOnPlan.setName(addOnPlanDto.getName());
        addOnPlan.setCountry(country);
        addOnPlan.setPriceAmount(addOnPlanDto.getPriceAmount());
        addOnPlan.setCreditsIncluded(addOnPlanDto.getCreditsIncluded());
        addOnPlan.setSortOrder(addOnPlanDto.getSortOrder());
        addOnPlan.setExpiresAt(addOnPlanDto.getExpiresAt());
        addOnPlan.setIsDefault(addOnPlanDto.getIsDefault());
        addOnPlan.setIsActive(addOnPlanDto.getIsActive());
        return addOnPlan;
    }

}
