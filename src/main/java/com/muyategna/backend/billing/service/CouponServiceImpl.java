package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.coupon.CouponDto;
import com.muyategna.backend.billing.entity.Coupon;
import com.muyategna.backend.billing.entity.CouponUsageTracker;
import com.muyategna.backend.billing.entity.DiscountPlan;
import com.muyategna.backend.billing.enums.CouponStatusEnum;
import com.muyategna.backend.billing.enums.UserEntityType;
import com.muyategna.backend.billing.gateways.dto.PaymentRequirement;
import com.muyategna.backend.billing.mapper.CouponMapper;
import com.muyategna.backend.billing.repository.CouponRepository;
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
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponUsageTrackerService couponUsageTrackerService;
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    @Autowired
    public CouponServiceImpl(CouponRepository couponRepository, CouponUsageTrackerService couponUsageTrackerService) {
        this.couponRepository = couponRepository;
        this.couponUsageTrackerService = couponUsageTrackerService;
    }

    @Override
    public CouponDto getCouponByCode(String couponCode) {
        log.info("Retrieving  coupon by coupon code: {}", couponCode);
        Coupon coupon = couponRepository.findCouponByCode(couponCode).orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
        log.info("Coupon found.");
        return CouponMapper.toDto(coupon);
    }

    @Override
    public Optional<Coupon> getAndValidateCoupon(String couponCode, PaymentRequirement paymentRequirement) {
        log.info("Retrieving coupon by coupon code: {}", couponCode);

        // Get country from context
        CountryDto countryDto = CountryContextHolder.getCountry();
        Optional<Coupon> coupon = getValidCoupon(couponRepository.findCouponByCodeAndCountryId(couponCode, countryDto.getId()).orElse(null), paymentRequirement, LocalDateTime.now());
        log.info("Coupon found");
        return coupon;
    }

    @Override
    public CouponDto getCouponById(Long couponId) {
        log.info("Retrieving coupon by id: {}", couponId);
        Coupon coupon = couponRepository.findCouponById(couponId).orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
        log.info("Coupon found with id: {}", couponId);
        return CouponMapper.toDto(coupon);
    }

    @Override
    public CouponDto getGlobalCouponForDiscountPlan(Long discountPlanId) {
        log.info("Retrieving global coupon for discount plan id: {}", discountPlanId);
        Coupon coupon = couponRepository.findGlobalCouponForDiscountPlan(discountPlanId).orElseThrow(() -> new ResourceNotFoundException("Global coupon not found"));
        log.info("Global coupon found for discount plan id: {}", discountPlanId);
        return CouponMapper.toDto(coupon);
    }


    @Override
    public BigDecimal computeAmountWithCoupon(BigDecimal amount, Coupon coupon) {
        if (coupon == null || coupon.getDiscountPlan() == null) {
            log.info("No coupon or discount plan provided, returning original amount: {}", amount);
            return amount;
        }

        DiscountPlan discountPlan = coupon.getDiscountPlan();
        BigDecimal totalDiscountAmount = BigDecimal.ZERO;

        if (discountPlan.getFixedValue() != null) {
            log.debug("Applying fixed discount: {}", discountPlan.getFixedValue());
            totalDiscountAmount = totalDiscountAmount.add(discountPlan.getFixedValue());
        }

        if (discountPlan.getPercentageValue() != null) {
            BigDecimal percentageDiscount = amount
                    .multiply(discountPlan.getPercentageValue().divide(ONE_HUNDRED, 4, RoundingMode.HALF_UP))
                    .setScale(2, RoundingMode.HALF_UP);
            log.debug("Applying percentage discount: {}% => {}", discountPlan.getPercentageValue(), percentageDiscount);
            totalDiscountAmount = totalDiscountAmount.add(percentageDiscount);
        }

        if (discountPlan.getMaxDiscountValue() != null) {
            log.debug("Applying max discount cap: {}", discountPlan.getMaxDiscountValue());
            totalDiscountAmount = totalDiscountAmount.min(discountPlan.getMaxDiscountValue());
        }

        BigDecimal finalAmount = amount.subtract(totalDiscountAmount);
        return BigDecimal.ZERO.max(finalAmount);
    }


    private Optional<Coupon> getValidCoupon(Coupon coupon, PaymentRequirement paymentRequirement, LocalDateTime now) {
        if (coupon == null) {
            log.info("No coupon provided");
            return Optional.empty();
        }

        if (coupon.getStatus() != CouponStatusEnum.ACTIVE) {
            log.info("Coupon {} is not active", coupon.getId());
            return Optional.empty();
        }

        if (coupon.getStartsAt() != null && now.isBefore(coupon.getStartsAt())) {
            log.info("Coupon {} is not yet valid", coupon.getId());
            return Optional.empty();
        }

        if (coupon.getExpiresAt() != null && now.isAfter(coupon.getExpiresAt())) {
            log.info("Coupon {} has expired", coupon.getId());
            return Optional.empty();
        }

        int totalUseCount = Optional.ofNullable(coupon.getTotalUseCount()).orElse(0);
        if (coupon.getUsageLimit() != null && totalUseCount >= coupon.getUsageLimit()) {
            log.info("Total usage limit reached for coupon {}", coupon.getId());
            return Optional.empty();
        }

        if (coupon.getPerUserLimit() != null &&
                isUsageLimitExceeded(coupon.getId(), paymentRequirement)) {
            log.info("Per-user usage limit reached for coupon {}", coupon.getId());
            return Optional.empty();
        }

        return Optional.of(coupon);
    }


    private boolean isUsageLimitExceeded(Long couponId, PaymentRequirement paymentRequirement) {
        UserEntityType couponUserType = paymentRequirement.getUserEntityType();
        Long couponUserId = paymentRequirement.getUserEntityId();

        CouponUsageTracker couponUsageTracker = couponUsageTrackerService.getUserCouponUsageCount(
                couponId, couponUserType, couponUserId);

        if (couponUsageTracker == null || couponUsageTracker.getUsageLimit() == null) {
            log.info("No usage limit for coupon {} or no tracker found for userType={}, userId={}",
                    couponId, couponUserType, couponUserId);
            return false;
        }

        boolean exceeded = couponUsageTracker.getUseCount() >= couponUsageTracker.getUsageLimit();
        log.info("Coupon {} usage for userType={}, userId={}: {}/{} (exceeded={})",
                couponId, couponUserType, couponUserId,
                couponUsageTracker.getUseCount(), couponUsageTracker.getUsageLimit(), exceeded);

        return exceeded;
    }


}
