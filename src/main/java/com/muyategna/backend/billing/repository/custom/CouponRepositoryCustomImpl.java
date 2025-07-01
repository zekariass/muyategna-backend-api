package com.muyategna.backend.billing.repository.custom;

import com.muyategna.backend.billing.entity.Coupon;
import com.muyategna.backend.billing.entity.QCoupon;
import com.muyategna.backend.billing.entity.QDiscountPlan;
import com.muyategna.backend.billing.enums.CouponStatusEnum;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class CouponRepositoryCustomImpl implements CouponRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public CouponRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Coupon> findCouponById(Long couponId) {
        QCoupon coupon = QCoupon.coupon;
        QDiscountPlan discountPlan = QDiscountPlan.discountPlan;
        LocalDateTime now = LocalDateTime.now();
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(coupon)
                        .leftJoin(coupon.discountPlan, discountPlan)
                        .where(
                                coupon.id.eq(couponId),
                                coupon.expiresAt.after(now),
                                coupon.startsAt.before(now),
                                coupon.usageLimit.gt(coupon.totalUseCount),
                                coupon.status.eq(CouponStatusEnum.ACTIVE)
                        )
                        .fetchOne());
    }

    @Override
    public Optional<Coupon> findCouponByCode(String couponCode) {
        QCoupon coupon = QCoupon.coupon;
        QDiscountPlan discountPlan = QDiscountPlan.discountPlan;
        LocalDateTime now = LocalDateTime.now();
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(coupon)
                        .leftJoin(coupon.discountPlan, discountPlan)
                        .where(
                                coupon.code.eq(couponCode),
                                coupon.expiresAt.after(now),
                                coupon.startsAt.before(now),
                                coupon.usageLimit.gt(coupon.totalUseCount),
                                coupon.status.eq(CouponStatusEnum.ACTIVE)
                        )
                        .fetchOne());
    }

    @Override
    public Optional<Coupon> findGlobalCouponForDiscountPlan(Long discountPlanId) {
        QCoupon coupon = QCoupon.coupon;
        QDiscountPlan discountPlan = QDiscountPlan.discountPlan;
        LocalDateTime now = LocalDateTime.now();
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(coupon)
                        .leftJoin(coupon.discountPlan, discountPlan)
                        .where(
                                coupon.discountPlan.id.eq(discountPlanId),
                                coupon.expiresAt.after(now),
                                coupon.startsAt.before(now),
                                coupon.usageLimit.gt(coupon.totalUseCount),
                                coupon.status.eq(CouponStatusEnum.ACTIVE),
                                coupon.isGlobal.isTrue()
                        )
                        .fetchOne());
    }
}
