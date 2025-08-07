package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.HoldCredit;
import com.muyategna.backend.billing.repository.HoldCreditRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HoldCreditServiceImpl implements HoldCreditService {

    private final HoldCreditRepository holdCreditRepository;

    @Autowired
    public HoldCreditServiceImpl(HoldCreditRepository holdCreditRepository) {
        this.holdCreditRepository = holdCreditRepository;
    }

    @Override
    public void save(HoldCredit holdCredit) {
        log.info("Saving hold credit...");
        holdCreditRepository.save(holdCredit);
        log.info("Hold credit saved.");
    }
}
