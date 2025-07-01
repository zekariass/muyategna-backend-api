package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.dto.coupon.CouponDto;
import com.muyategna.backend.billing.entity.Coupon;
import com.muyategna.backend.billing.mapper.CouponMapper;
import com.muyategna.backend.billing.repository.CouponRepository;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Autowired
    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public CouponDto getCouponByCode(String couponCode) {
        log.info("Retrieving coupon by coupon code: {}", couponCode);
        Coupon coupon = couponRepository.findCouponByCode(couponCode).orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
        log.info("Coupon found");
        return CouponMapper.toDto(coupon);
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

}
