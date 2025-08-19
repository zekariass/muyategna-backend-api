package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.discount_plan.DiscountPlanLocalizedDto;
import com.muyategna.backend.billing.entity.DiscountPlan;
import com.muyategna.backend.billing.entity.DiscountPlanTranslation;
import com.muyategna.backend.billing.entity.DiscountUsageTracker;
import com.muyategna.backend.billing.gateways.dto.PaymentRequirement;
import com.muyategna.backend.billing.mapper.DiscountPlanMapper;
import com.muyategna.backend.billing.repository.DiscountPlanRepository;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class DiscountPlanServiceImpl implements DiscountPlanService {

    private final DiscountPlanRepository repository;
    private final DiscountPlanTranslationService discountPlanTranslationService;
    private final DiscountUsageTrackerService discountUsageTrackerService;

    @Autowired
    public DiscountPlanServiceImpl(DiscountPlanRepository repository, DiscountPlanTranslationService discountPlanTranslationService, DiscountUsageTrackerService discountUsageTrackerService) {
        this.repository = repository;
        this.discountPlanTranslationService = discountPlanTranslationService;
        this.discountUsageTrackerService = discountUsageTrackerService;
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

    @Override
    public DiscountPlan getDiscountPlan(Long discountPlanId) {
        log.info("Retrieving discount  plan with id: {}", discountPlanId);

        // Get the current country context
        CountryDto countryDto = CountryContextHolder.getCountry();
        return repository.findByIdAndCountryId(discountPlanId, countryDto.getId()).orElse(null);
    }

    @Override
    public Optional<DiscountPlan> getAndValidateDiscountPlan(Long discountPlanId, PaymentRequirement paymentRequirement) {
        log.info("Validating discount plan with id: {}", discountPlanId);
        DiscountPlan discountPlan = getDiscountPlan(discountPlanId);
        return validateDiscountPlan(discountPlan, paymentRequirement, LocalDateTime.now());
    }

    @Override
    public BigDecimal computeAmountWithDiscountPlan(BigDecimal amount, DiscountPlan discountPlan) {
        BigDecimal totalDiscountAmount = BigDecimal.ZERO;
        if (discountPlan == null) {
            log.info("No discount plan provided, returning original amount: {}", amount);
            return amount;
        }

        if (discountPlan.getFixedValue() != null) {
            totalDiscountAmount = discountPlan.getFixedValue();
        }

        if (discountPlan.getPercentageValue() != null) {
            BigDecimal percentageDiscount = amount.multiply(
                            discountPlan.getPercentageValue().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP))
                    .setScale(2, RoundingMode.HALF_UP);

            totalDiscountAmount = totalDiscountAmount.add(percentageDiscount);
        }

        if (discountPlan.getMaxDiscountValue() != null) {
            totalDiscountAmount = totalDiscountAmount.min(discountPlan.getMaxDiscountValue());
        }

        return BigDecimal.ZERO.max(amount.subtract(totalDiscountAmount));
    }

    private Optional<DiscountPlan> validateDiscountPlan(DiscountPlan plan, PaymentRequirement paymentRequirement, LocalDateTime now) {
        if (plan == null || !Boolean.TRUE.equals(plan.getIsActive())) {
            log.info("Inactive or missing discount plan: {}", plan != null ? plan.getId() : null);
            return Optional.empty();
        }

        if (plan.getStartsAt() != null && now.isBefore(plan.getStartsAt())) {
            log.info("Discount plan {} has not started yet", plan.getId());
            return Optional.empty();
        }

        if (plan.getExpiresAt() != null && now.isAfter(plan.getExpiresAt())) {
            log.info("Discount plan {} has expired", plan.getId());
            return Optional.empty();
        }

        if (plan.getUsageLimit() != null && plan.getUsageLimit() > 0 &&
                plan.getTotalUseCount() >= plan.getUsageLimit()) {
            log.info("Total usage limit exceeded for discount plan {}", plan.getId());
            return Optional.empty();
        }

        if (plan.getPerUserLimit() != null && plan.getPerUserLimit() > 0 &&
                isDiscountUsageCountExceeded(plan.getId(), paymentRequirement)) {
            log.info("Per-user usage limit exceeded for discount plan {}", plan.getId());
            return Optional.empty();
        }

        if (Boolean.TRUE.equals(plan.getCouponRequired())) {
            log.info("Coupon is required for discount plan {}", plan.getId());
            return Optional.empty();
        }

        return Optional.of(plan);
    }


    private boolean isDiscountUsageCountExceeded(Long discountPlanId, PaymentRequirement paymentRequirement) {
        log.info("Getting user usage count for discount plan with id: {}", discountPlanId);
        DiscountUsageTracker discountUsageTracker = discountUsageTrackerService.getDiscountUsageTracker(
                discountPlanId,
                paymentRequirement.getUserEntityType(),
                paymentRequirement.getUserEntityId()
        );

        return discountUsageTracker != null && discountUsageTracker.getUseCount() >= discountUsageTracker.getUsageLimit();
    }
}
