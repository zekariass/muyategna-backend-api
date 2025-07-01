package com.muyategna.backend.billing.mapper;

import com.muyategna.backend.billing.dto.discount_plan.DiscountPlanCreateDto;
import com.muyategna.backend.billing.dto.discount_plan.DiscountPlanDto;
import com.muyategna.backend.billing.dto.discount_plan.DiscountPlanLocalizedDto;
import com.muyategna.backend.billing.dto.discount_plan.DiscountPlanUpdateDto;
import com.muyategna.backend.billing.entity.DiscountPlan;
import com.muyategna.backend.billing.entity.DiscountPlanTranslation;
import com.muyategna.backend.location.entity.Country;

public final class DiscountPlanMapper {

    public static DiscountPlanDto toDto(DiscountPlan discountPlan) {
        return DiscountPlanDto.builder()
                .id(discountPlan.getId())
                .countryId(discountPlan.getCountry().getId())
                .name(discountPlan.getName())
                .description(discountPlan.getDescription())
                .fixedValue(discountPlan.getFixedValue())
                .percentageValue(discountPlan.getPercentageValue())
                .startsAt(discountPlan.getStartsAt())
                .expiresAt(discountPlan.getExpiresAt())
                .usageLimit(discountPlan.getUsageLimit())
                .perUserLimit(discountPlan.getPerUserLimit())
                .totalUseCount(discountPlan.getTotalUseCount())
                .maxDiscountValue(discountPlan.getMaxDiscountValue())
                .isActive(discountPlan.getIsActive())
                .couponRequired(discountPlan.getCouponRequired())
                .appliesTo(discountPlan.getAppliesTo())
                .createdAt(discountPlan.getCreatedAt())
                .updatedAt(discountPlan.getUpdatedAt())
                .build();
    }


    public static DiscountPlanLocalizedDto toLocalizedDto(
            DiscountPlan discountPlan,
            DiscountPlanTranslation translation) {
        return DiscountPlanLocalizedDto.builder()
                .id(discountPlan.getId())
                .countryId(discountPlan.getCountry().getId())
                .name(translation.getDisplayName())
                .description(translation.getDescription())
                .fixedValue(discountPlan.getFixedValue())
                .percentageValue(discountPlan.getPercentageValue())
                .isActive(discountPlan.getIsActive())
                .couponRequired(discountPlan.getCouponRequired())
                .appliesTo(discountPlan.getAppliesTo())
                .build();
    }

    public static DiscountPlan toEntity(DiscountPlanDto discountPlanDto,
                                        Country country) {
        DiscountPlan discountPlan = new DiscountPlan();
        discountPlan.setName(discountPlanDto.getName());
        discountPlan.setCountry(country);
        discountPlan.setDescription(discountPlanDto.getDescription());
        discountPlan.setFixedValue(discountPlanDto.getFixedValue());
        discountPlan.setPercentageValue(discountPlanDto.getPercentageValue());
        discountPlan.setStartsAt(discountPlanDto.getStartsAt());
        discountPlan.setExpiresAt(discountPlanDto.getExpiresAt());
        discountPlan.setUsageLimit(discountPlanDto.getUsageLimit());
        discountPlan.setPerUserLimit(discountPlanDto.getPerUserLimit());
        discountPlan.setMaxDiscountValue(discountPlanDto.getMaxDiscountValue());
        discountPlan.setIsActive(discountPlanDto.getIsActive());
        discountPlan.setCreatedAt(discountPlanDto.getCreatedAt());
        discountPlan.setUpdatedAt(discountPlanDto.getUpdatedAt());
        discountPlan.setCouponRequired(discountPlanDto.getCouponRequired());
        discountPlan.setAppliesTo(discountPlanDto.getAppliesTo());
        return discountPlan;
    }


    public static DiscountPlan toEntity(DiscountPlanUpdateDto discountPlanDto,
                                        Country country) {
        DiscountPlan discountPlan = new DiscountPlan();
        discountPlan.setName(discountPlanDto.getName());
        discountPlan.setCountry(country);
        discountPlan.setDescription(discountPlanDto.getDescription());
        discountPlan.setFixedValue(discountPlanDto.getFixedValue());
        discountPlan.setPercentageValue(discountPlanDto.getPercentageValue());
        discountPlan.setStartsAt(discountPlanDto.getStartsAt());
        discountPlan.setExpiresAt(discountPlanDto.getExpiresAt());
        discountPlan.setUsageLimit(discountPlanDto.getUsageLimit());
        discountPlan.setPerUserLimit(discountPlanDto.getPerUserLimit());
        discountPlan.setMaxDiscountValue(discountPlanDto.getMaxDiscountValue());
        discountPlan.setIsActive(discountPlanDto.getIsActive());
        discountPlan.setCouponRequired(discountPlanDto.getCouponRequired());
        discountPlan.setAppliesTo(discountPlanDto.getAppliesTo());
        return discountPlan;
    }


    public static DiscountPlan toEntity(DiscountPlanCreateDto discountPlanDto,
                                        Country country) {
        DiscountPlan discountPlan = new DiscountPlan();
        discountPlan.setName(discountPlanDto.getName());
        discountPlan.setCountry(country);
        discountPlan.setDescription(discountPlanDto.getDescription());
        discountPlan.setFixedValue(discountPlanDto.getFixedValue());
        discountPlan.setPercentageValue(discountPlanDto.getPercentageValue());
        discountPlan.setStartsAt(discountPlanDto.getStartsAt());
        discountPlan.setExpiresAt(discountPlanDto.getExpiresAt());
        discountPlan.setUsageLimit(discountPlanDto.getUsageLimit());
        discountPlan.setPerUserLimit(discountPlanDto.getPerUserLimit());
        discountPlan.setMaxDiscountValue(discountPlanDto.getMaxDiscountValue());
        discountPlan.setIsActive(discountPlanDto.getIsActive());
        discountPlan.setCouponRequired(discountPlanDto.getCouponRequired());
        discountPlan.setAppliesTo(discountPlanDto.getAppliesTo());
        return discountPlan;
    }
}
