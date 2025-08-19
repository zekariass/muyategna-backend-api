package com.muyategna.backend.billing.gateways.mappers.bank_transfer_transaction;

import com.muyategna.backend.billing.entity.BankTransferTransaction;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.billing.gateways.dto.bank_transfer_transaction.BankTransferTransactionCreateDto;

public final class BankTransferTransactionMapper {

    public static BankTransferTransaction toEntity(
            BankTransferTransaction entity,
            BankTransferTransactionCreateDto dto,
            Transaction transaction) {
        if (dto == null) {
            return null;
        }

        entity.setAmountPaid(dto.getAmountPaid());
        entity.setCurrency(dto.getCurrency());
        entity.setTransaction(transaction);
        entity.setBankReferenceNumber(dto.getBankReferenceNumber());
        entity.setPayerFullName(dto.getPayerFullName());
        entity.setPayerBankName(dto.getPayerBankName());
        entity.setPayerBankBranchName(dto.getPayerBankBranchName());
        entity.setPayerBankAccountNumber(dto.getPayerBankAccountNumber());
        entity.setExtraData(null);

        return entity;
    }
}
