package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.ServiceProviderWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ServiceProviderWalletRepository extends JpaRepository<ServiceProviderWallet, Long> {

    @Query(
            """
                    SELECT spw
                    FROM ServiceProviderWallet spw
                    JOIN FETCH spw.provider
                    LEFT JOIN FETCH spw.paygoCredit
                    LEFT JOIN FETCH spw.subscription
                    WHERE spw.provider.id = :serviceProviderId
                    """
    )
    Optional<ServiceProviderWallet> findByServiceProviderIdWithRelatedEntities(Long serviceProviderId);
}
