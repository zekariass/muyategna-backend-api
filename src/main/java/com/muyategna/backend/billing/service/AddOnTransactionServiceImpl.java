package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.AddOnPlan;
import com.muyategna.backend.billing.entity.AddOnTransaction;
import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.billing.mapper.AddOnPlanMapper;
import com.muyategna.backend.billing.repository.AddOnTransactionRepository;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AddOnTransactionServiceImpl implements AddOnTransactionService {

    private final AddOnTransactionRepository addOnTransactionRepository;
    private final AddOnPlanService addOnPlanService;

    @Autowired
    public AddOnTransactionServiceImpl(AddOnTransactionRepository addOnTransactionRepository, AddOnPlanService addOnPlanService) {
        this.addOnTransactionRepository = addOnTransactionRepository;
        this.addOnPlanService = addOnPlanService;
    }

    @Override
    public void createAddOnTransaction(Invoice invoice, Transaction transaction, ServiceProvider serviceProvider) {
        AddOnTransaction addOnTransaction = new AddOnTransaction();
        AddOnPlan addOnPlan = AddOnPlanMapper.toEntity(addOnPlanService.getAddOnPlan(invoice.getProductPlanId()));

        if (addOnPlan == null) {
            throw new ResourceNotFoundException("AddOnPlan not found for productPlanId: " + invoice.getProductPlanId());
        }

        addOnTransaction.setTransaction(transaction);
        addOnTransaction.setAddOnPlan(addOnPlan);
        addOnTransaction.setExtraData(null);
        addOnTransaction.setProvider(serviceProvider);
        addOnTransaction.setCreditsAmount(addOnPlan.getCreditsIncluded());

        addOnTransactionRepository.save(addOnTransaction);
    }
}
