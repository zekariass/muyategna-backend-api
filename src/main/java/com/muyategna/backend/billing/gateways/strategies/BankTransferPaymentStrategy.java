package com.muyategna.backend.billing.gateways.strategies;

import com.muyategna.backend.billing.entity.BankTransferTransaction;
import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.PaymentMethod;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.billing.enums.*;
import com.muyategna.backend.billing.gateways.dto.PaymentRequirement;
import com.muyategna.backend.billing.gateways.dto.bank_transfer_transaction.BankTransferTransactionCreateDto;
import com.muyategna.backend.billing.gateways.dto.bank_transfer_transaction.BankTransferTransactionProofQueueMessage;
import com.muyategna.backend.billing.gateways.dto.platform_bank_information.PlatformBankInformationViewDto;
import com.muyategna.backend.billing.gateways.mappers.bank_transfer_transaction.BankTransferTransactionMapper;
import com.muyategna.backend.billing.gateways.service.BankTransferService;
import com.muyategna.backend.billing.gateways.service.producer.PaymentProducerService;
import com.muyategna.backend.billing.service.*;
import com.muyategna.backend.common.Constants;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.service.ServiceProviderServiceInterface;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.muyategna.backend.user.entity.UserProfile;
import com.muyategna.backend.user.service.UserProfileService;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Slf4j
public class BankTransferPaymentStrategy implements PaymentStrategy, BankTransferService {

    private final ServiceProviderServiceInterface serviceProviderService;
    private final UserProfileService userProfileService;
    private final PaymentMethodService paymentMethodService;
    private final InvoiceService invoiceService;
    private final TransactionService transactionService;
    private final PlatformBankInformationService platformBankInformationService;
    private final BankTransferTransactionService bankTransferTransactionService;
    private final PaymentProducerService paymentProducerService;

    @Autowired
    public BankTransferPaymentStrategy(ServiceProviderServiceInterface serviceProviderService, UserProfileService userProfileService, PaymentMethodService paymentMethodService, InvoiceService invoiceService, TransactionService transactionService, PlatformBankInformationService platformBankInformationService, BankTransferTransactionService bankTransferTransactionService, PaymentProducerService paymentProducerService) {
        this.serviceProviderService = serviceProviderService;
        this.userProfileService = userProfileService;
        this.paymentMethodService = paymentMethodService;
        this.invoiceService = invoiceService;
        this.transactionService = transactionService;
        this.platformBankInformationService = platformBankInformationService;
        this.bankTransferTransactionService = bankTransferTransactionService;
        this.paymentProducerService = paymentProducerService;
    }

    @Transactional
    @Override
    public Map<String, Object> initializePayment(PaymentRequirement paymentRequirement) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Initializing payment for bank transfer transaction <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        ProductType productType = paymentRequirement.getProductType();
        Long paymentMethodId = paymentRequirement.getPaymentMethodId();
        Long addOnPlanId = paymentRequirement.getAddOnPlanId();
        Long discountPlanId = paymentRequirement.getDiscountPlanId();
        Long userEntityId = paymentRequirement.getUserEntityId();
        UserEntityType userEntityType = paymentRequirement.getUserEntityType();

        ServiceProvider serviceProvider = null;
        UserProfile userProfile = null;
        if (userEntityId != null) {
            if (userEntityType == UserEntityType.SERVICE_PROVIDER) {
                serviceProvider = serviceProviderService.getServiceProviderWithBusinessAddressById(userEntityId);
            } else if (userEntityType == UserEntityType.CUSTOMER) {
                userProfile = userProfileService.getUserProfile(userEntityId);
            }
        }

        if (serviceProvider == null && userProfile == null) {
            throw new BadRequestException("Valid user entity id and user entity type are required for payment initialization");
        }

        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethod(paymentMethodId);

        // Create invoice and transaction
        Invoice invoice = invoiceService.createInvoice(paymentRequirement);
        if (invoice == null) {
            throw new IllegalStateException("Failed to create invoice");
        }

        Transaction transaction = transactionService.createTransaction(
                invoice,
                AccountingTypeEnum.DR,
                paymentRequirement,
                TransactionStatusEnum.AWAITING_BANK_TRANSFER,
                paymentMethod,
                TransactionTypeEnum.PAYOUT
        );
        if (transaction == null) {
            throw new IllegalStateException("Failed to create transaction");
        }

        log.info("Initialized invoice ID: {}, transaction ID: {}", invoice.getId(), transaction.getId());

        // Create initial record in bank transfer transaction table
        BankTransferTransaction bankTransferTransaction = bankTransferTransactionService
                .createPendingBankTransferTransaction(transaction, invoice, serviceProvider, userProfile);

        // Get list of bank information
        List<PlatformBankInformationViewDto> bankInformationList = platformBankInformationService
                .getPlatformBankInformationByCountryIdAndIsActive(
                        CountryContextHolder.getCountry().getId(),
                        true);

        Map<String, Object> result = new HashMap<>();

        result.put("amountToPay", bankTransferTransaction.getAmountDue());
        result.put("invoiceNumber", invoice.getInvoiceNumber());
        result.put("txnRef", bankTransferTransaction.getTxnRef());
        result.put("bankInfo", bankInformationList);


        return result;
    }

    @Override
    public void processWebhook(Map<String, Object> payload) {

    }

    @Override
    public boolean verifyTransaction(Map<String, Object> params) {
        return false;
    }

    @Override
    public void reportBankTransferPayment(BankTransferTransactionCreateDto bankTransferDto) {
        if (bankTransferDto == null) {
            throw new BadRequestException("Bank transfer DTO cannot be null");
        }

        // Get uploaded file
        MultipartFile file = bankTransferDto.getProofOfPaymentFile();

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Proof of payment file is required");
        }

        String ext = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
        boolean extIsValid = Constants.IMAGE_EXTENSIONS.contains(ext) ||
                Constants.VIDEO_EXTENSIONS.contains(ext) ||
                Constants.DOCUMENT_EXTENSIONS.contains(ext);

        if (!extIsValid) {
            throw new BadRequestException("File extension is not valid the file type uploaded");
        }

        // Validate file size
        if (Constants.IMAGE_EXTENSIONS.contains(ext) && file.getSize() > Constants.IMAGE_MAX_SIZE) {
            throw new BadRequestException("File is too large. Maximum size for image files is " + Constants.IMAGE_MAX_SIZE / (1024 * 1024) + " MB");
        } else if (Constants.VIDEO_EXTENSIONS.contains(ext) && file.getSize() > Constants.VIDEO_MAX_SIZE) {
            throw new BadRequestException("File is too large. Maximum size for video files is " + Constants.VIDEO_MAX_SIZE / (1024 * 1024) + " MB");
        } else if (Constants.DOCUMENT_EXTENSIONS.contains(ext) && file.getSize() > Constants.DOCUMENT_MAX_SIZE) {
            throw new BadRequestException("File is too large. Maximum size for document files is " + Constants.DOCUMENT_MAX_SIZE / (1024 * 1024) + " MB");
        }


        // Get BankTransferTransaction from DB with transaction
        BankTransferTransaction existingTransaction = bankTransferTransactionService
                .getBankTransferTransactionByTxnRefWithTransaction(bankTransferDto.getTxnRef())
                .orElseThrow(() -> new ResourceNotFoundException("Bank transfer transaction has not been created before. Please create a pending bank transfer transaction first."));

        BankTransferTransaction bankTransferTransaction = BankTransferTransactionMapper.toEntity(existingTransaction, bankTransferDto, existingTransaction.getTransaction());
        bankTransferTransaction.setStatus(BankTransferStatus.REPORTED);

        bankTransferTransactionService.saveBankTransferTransaction(bankTransferTransaction);

        try {


            // Enqueue the proof of payment file for upload
            BankTransferTransactionProofQueueMessage queueMessage = BankTransferTransactionProofQueueMessage.builder()
                    .transactionId(existingTransaction.getId())
                    .txnRef(existingTransaction.getTxnRef())
                    .fileContent(file.getBytes())
                    .originalFilename(file.getOriginalFilename())
                    .newFilename(UUID.randomUUID().toString())
                    .extension(ext)
                    .contentType(file.getContentType())
                    .build();

            paymentProducerService.enqueueBankTransferPaymentProofUpload(queueMessage);

        } catch (IOException e) {
            log.error("Error reading proof of payment file: {}", e.getMessage());
            throw new BadRequestException("Failed to read proof of payment file");
        } catch (Exception e) {
            log.error("Error processing bank transfer payment: {}", e.getMessage());
            throw new RuntimeException("Failed to process bank transfer payment", e);
        }


    }


    private String getFileExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        if (idx < 0 || idx == filename.length() - 1) {
            throw new IllegalArgumentException("Invalid file extension in filename: " + filename);
        }
        return filename.substring(idx + 1);
    }


    @Override
    public void verifyBankTransferPayment(String txnRef) {
        if (txnRef == null || txnRef.isEmpty()) {
            throw new BadRequestException("Transaction reference cannot be null or empty");
        }

        BankTransferTransaction bankTransferTransaction = bankTransferTransactionService
                .getBankTransferTransactionByTxnRefWithTransaction(txnRef)
                .orElseThrow(() -> new ResourceNotFoundException("Bank transfer transaction not found for transaction ref: " + txnRef));

        if (bankTransferTransaction.getStatus() != BankTransferStatus.REPORTED) {
            throw new BadRequestException("Bank transfer transaction is not in reported status");
        }

        // Update the status to VERIFIED
        bankTransferTransaction.setStatus(BankTransferStatus.VERIFIED);
        bankTransferTransaction.setVerifiedAt(LocalDateTime.now());
        if (bankTransferTransaction.getAmountPaid().compareTo(bankTransferTransaction.getAmountDue()) < 0) {
            throw new BadRequestException("Amount paid cannot be less than the amount due for bank transfer transaction");
        }
        bankTransferTransactionService.saveBankTransferTransaction(bankTransferTransaction);

        // Update the transaction status
        Transaction transaction = bankTransferTransaction.getTransaction();
        transaction.setStatus(TransactionStatusEnum.COMPLETED);
        transactionService.saveTransaction(transaction);

        // Update the invoice status
        Invoice invoice = transaction.getInvoice();
        invoice.setStatus(InvoiceStatusEnum.PAID);
        invoiceService.saveInvoice(invoice);

        log.info("Bank transfer payment verified for transaction ref: {}", txnRef);
    }

    @Override
    public void cancelBankTransferPayment(String txnRef) {
        if (txnRef == null || txnRef.isEmpty()) {
            throw new BadRequestException("Transaction reference cannot be null or empty");
        }

        BankTransferTransaction bankTransferTransaction = bankTransferTransactionService
                .getBankTransferTransactionByTxnRefWithTransaction(txnRef)
                .orElseThrow(() -> new ResourceNotFoundException("Bank transfer transaction not found for transaction ref: " + txnRef));

        if (bankTransferTransaction.getStatus() == BankTransferStatus.VERIFIED
                || bankTransferTransaction.getStatus() == BankTransferStatus.FAILED) {
            throw new IllegalStateException("Bank transfer transaction is already {%s}, cannot cancel".formatted(bankTransferTransaction.getStatus()));
        }

        // Update the status to VERIFIED
        bankTransferTransaction.setStatus(BankTransferStatus.CANCELLED);
        bankTransferTransactionService.saveBankTransferTransaction(bankTransferTransaction);

        // Update the transaction status
        Transaction transaction = bankTransferTransaction.getTransaction();
        transaction.setStatus(TransactionStatusEnum.CANCELLED);
        transactionService.saveTransaction(transaction);

        // Update the invoice status
        Invoice invoice = transaction.getInvoice();
        invoice.setStatus(InvoiceStatusEnum.CANCELLED);
        invoiceService.saveInvoice(invoice);

        log.info("Bank transfer payment cancelled for transaction ref: {}", txnRef);
    }
}
