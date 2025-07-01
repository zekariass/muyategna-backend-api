package com.muyategna.backend.billing.repository.custom;

import com.muyategna.backend.billing.entity.PaymentMethod;
import com.muyategna.backend.billing.entity.QPaymentMethod;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class PaymentMethodRepositoryCustomImpl implements PaymentMethodRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public PaymentMethodRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<PaymentMethod> findPaymentMethodByIdAndCountryId(Long paymentMethodId, Long countryId) {
        QPaymentMethod paymentMethod = QPaymentMethod.paymentMethod;
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(paymentMethod)
                        .where(
                                paymentMethod.id.eq(paymentMethodId),
                                paymentMethod.country.id.eq(countryId)
                        )
                        .fetchOne()
        );
    }

    @Override
    public List<PaymentMethod> findPaymentMethodsByCountryId(Long countryId) {
        QPaymentMethod paymentMethod = QPaymentMethod.paymentMethod;
        return jpaQueryFactory
                .selectFrom(paymentMethod)
                .where(
                        paymentMethod.country.id.eq(countryId)
                )
                .fetch();

    }
}
