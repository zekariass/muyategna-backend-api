package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.AddOnTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddOnTransactionRepository extends JpaRepository<AddOnTransaction, Long> {
}
