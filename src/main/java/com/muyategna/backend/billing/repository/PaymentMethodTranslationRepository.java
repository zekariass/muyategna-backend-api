package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.PaymentMethodTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodTranslationRepository extends JpaRepository<PaymentMethodTranslation, Long> {

    Optional<PaymentMethodTranslation> findByPaymentMethod_Id(Long paymentMethodId);

    Optional<PaymentMethodTranslation> findByPaymentMethod_IdAndLanguage_Id(Long paymentMethodId, long languageId);

    List<PaymentMethodTranslation> findByPaymentMethod_IdInAndLanguage_Id(List<Long> paymentMethodIds, long id);
}
