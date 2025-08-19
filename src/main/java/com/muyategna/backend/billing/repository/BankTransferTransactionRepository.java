package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.BankTransferTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BankTransferTransactionRepository extends JpaRepository<BankTransferTransaction, Long> {
    Optional<BankTransferTransaction> findByTxnRef(String txnRef);

    @Query(
            """
                    SELECT DISTINCT btt FROM BankTransferTransaction btt
                    JOIN FETCH btt.transaction
                    WHERE btt.txnRef = :txnRef
                    """
    )
    Optional<BankTransferTransaction> findByTxnRefWithTransaction(@Param("txnRef") String txnRef);
}
