package com.muyategna.backend.billing.repository;

import com.muyategna.backend.billing.entity.PlatformBankInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatformBankInformationRepository extends JpaRepository<PlatformBankInformation, Long> {
    List<PlatformBankInformation> findByCountryIdAndIsActive(Long countryId, boolean isActive);
}
