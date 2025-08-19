package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTxnRef(String txnRef);

    @Query(
            """
                        SELECT t FROM Transaction t
                        JOIN FETCH t.invoice
                        WHERE t.txnRef = :txnRef
                    """
    )
    Optional<Transaction> findByTxnRefWithInvoice(@Param("txnRef") String txnRef);
}
