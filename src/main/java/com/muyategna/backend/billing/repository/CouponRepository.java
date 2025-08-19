package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.Coupon;
import com.muyategna.backend.billing.repository.custom.CouponRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponRepositoryCustom {

    @Query(
            """
                    SELECT DISTINCT c FROM Coupon c
                    WHERE c.code = :couponCode AND c.discountPlan.country.id = :country
                    """
    )
    Optional<Coupon> findCouponByCodeAndCountryId(String couponCode, Long countryId);
}
