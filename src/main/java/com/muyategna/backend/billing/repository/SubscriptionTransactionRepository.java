package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.SubscriptionTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionTransactionRepository extends JpaRepository<SubscriptionTransaction, Long> {
}
