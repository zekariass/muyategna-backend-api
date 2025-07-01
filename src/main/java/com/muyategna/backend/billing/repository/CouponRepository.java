package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.Coupon;
import com.muyategna.backend.billing.repository.custom.CouponRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponRepositoryCustom {
}
