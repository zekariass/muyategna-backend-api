package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.PayGoCredit;
import com.muyategna.backend.billing.repository.PayGoCreditRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayGoCreditServiceImpl implements PayGoCreditService {
    private final PayGoCreditRepository payGoCreditRepository;

    public PayGoCreditServiceImpl(PayGoCreditRepository payGoCreditRepository) {
        this.payGoCreditRepository = payGoCreditRepository;
    }

    @Override
    public void save(PayGoCredit serviceProviderPayGoCredit) {
        log.info("Saving payGo credit with id: {}", serviceProviderPayGoCredit.getId());
        payGoCreditRepository.save(serviceProviderPayGoCredit);
    }
}
