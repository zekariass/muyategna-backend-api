package com.muyategna.backend.billing.gateways.service;

import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.ServiceProviderSubscription;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.billing.enums.ProductType;
import com.muyategna.backend.billing.enums.TransactionStatusEnum;
import com.muyategna.backend.billing.enums.UserEntityType;
import com.muyategna.backend.billing.service.*;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.service.ServiceProviderServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class PaymentServiceUtil {

    private final TransactionService transactionService;
    private final ServiceProviderServiceInterface serviceProviderService;
    private final PayGoCreditService payGoCreditService;
    private final AddOnTransactionService addOnTransactionService;
    private final ServiceProviderSubscriptionService subscriptionService;
    private final SubscriptionTransactionService subscriptionTransactionService;
    private final ServiceProviderWalletService walletService;
    private final CouponUsageTrackerService couponUsageTrackerService;
    private final DiscountUsageTrackerService discountUsageTrackerService;

    @Autowired
    public PaymentServiceUtil(TransactionService transactionService, ServiceProviderServiceInterface serviceProviderService, PayGoCreditService payGoCreditService, AddOnTransactionService addOnTransactionService, ServiceProviderSubscriptionService subscriptionService, SubscriptionTransactionService subscriptionTransactionService, ServiceProviderWalletService walletService, CouponUsageTrackerService couponUsageTrackerService, DiscountUsageTrackerService discountUsageTrackerService) {
        this.transactionService = transactionService;
        this.serviceProviderService = serviceProviderService;
        this.payGoCreditService = payGoCreditService;
        this.addOnTransactionService = addOnTransactionService;
        this.subscriptionService = subscriptionService;
        this.subscriptionTransactionService = subscriptionTransactionService;
        this.walletService = walletService;
        this.couponUsageTrackerService = couponUsageTrackerService;
        this.discountUsageTrackerService = discountUsageTrackerService;
    }

    @Transactional
    public void processForWebhook(Map<String, Object> payload, String gatewayName) {
        if (payload == null || payload.isEmpty()) {
            log.warn("Payload is null for gateway: {}", gatewayName);
            return;
        }

        String txnRef;
        if ("chapa".equalsIgnoreCase(gatewayName)) {
            txnRef = (String) payload.get("tx_ref");
        } else if ("pesapal".equalsIgnoreCase(gatewayName)) {
            txnRef = (String) payload.get("orderMerchantReference");
        } else {
            log.warn("Unsupported gateway for webhook processing: {}", gatewayName);
            return;
        }

        if (txnRef == null || txnRef.isBlank()) {
            log.warn("Transaction reference is missing for gateway: {}", gatewayName);
            return;
        }

        transactionService.getTransactionByTxnRef(txnRef).ifPresentOrElse(txn -> {
            txn.setStatus(TransactionStatusEnum.WEBHOOK_RECEIVED);
            txn.setWebhookResponseMap(payload);
            transactionService.saveTransaction(txn);
            log.info("Transaction {} marked as COMPLETED for gateway: {}", txnRef, gatewayName);
        }, () -> {
            log.warn("Transaction not found for txnRef: {}", txnRef);
        });
    }


//    public void processForWebhookFailure(Map<String, Object> payload, String gatewayName) {
//        if (payload == null) {
//            log.warn("Payload is null for gateway: {}", gatewayName);
//            return;
//        }
//
//        String txnRef;
//        if ("chapa".equalsIgnoreCase(gatewayName)) {
//            txnRef = (String) payload.get("tx_ref");
//        } else {
//            txnRef = null;
//        }
//
//        if (txnRef == null || txnRef.isBlank()) {
//            log.warn("Transaction reference is missing for gateway: {}", gatewayName);
//            return;
//        }
//
//        transactionService.getTransactionByTxnRef(txnRef).ifPresentOrElse(txn -> {
//            txn.setStatus(TransactionStatusEnum.WEBHOOK_RECEIVED);
//            txn.setWebhookResponseMap(payload);
//            transactionService.saveTransaction(txn);
//            log.info("Transaction {} marked as COMPLETED for gateway: {}", txnRef, gatewayName);
//        }, () -> {
//            log.warn("Transaction not found for txnRef: {}", txnRef);
//        });
//    }


    @Transactional
    public void processForVerificationSuccess(Map<String, Object> body, String gatewayName) {
        String txnRef = "";

        if (gatewayName.equalsIgnoreCase("chapa")) {
            Map<String, Object> data = (Map<String, Object>) body.get("data");
            txnRef = (String) data.get("tx_ref");
        } else if (gatewayName.equalsIgnoreCase("pesapal")) {
            txnRef = (String) body.get("merchant_reference");
        } else {
            log.warn("Unsupported gateway for verification success processing: {}", gatewayName);
            return;
        }

        Optional<Transaction> transaction = transactionService.getTransactionByTxnRefWithInvoice(txnRef);

        if (transaction.isPresent()) {
            System.out.println("======================================================> YYY Transaction Reference: " + txnRef);

            Transaction txn = transaction.get();
            txn.setIsTxnVerified(true);
            txn.setTxnVerificationResponseMap(body);
            txn.setStatus(TransactionStatusEnum.COMPLETED);

            log.info("Updating transaction for successful verification: {}", txnRef);
            Transaction updatedTransaction = transactionService.saveTransaction(txn);

            Invoice invoice = txn.getInvoice();

            UserEntityType userEntityType = invoice.getPayerEntityType();
            ProductType productType = invoice.getProductType();

            if (userEntityType.equals(UserEntityType.SERVICE_PROVIDER)) {
                Long serviceProviderId = invoice.getPayerEntityId();
                Optional<ServiceProvider> serviceProvider = serviceProviderService.getServiceProvider(serviceProviderId);
                if (serviceProvider.isEmpty()) {
                    log.warn("Service provider not found for update the transaction with txnRef: {}", txnRef);
                    return;
                }
                // If transaction is for Service Provider, Create and update the following tables for successful and verified transaction
                // 1. AddOnTransaction
                if (productType.equals(ProductType.ADD_ON)) {
                    createOrUpdatePayGoCredit(invoice, updatedTransaction, serviceProvider.get());
                    createAddOnTransaction(invoice, updatedTransaction, serviceProvider.get());
                }
                // 2. SubscriptionTransaction
                if (productType.equals(ProductType.SUBSCRIPTION)) {
                    ServiceProviderSubscription subscription = createOrUpdateServiceProviderSubscription(invoice, updatedTransaction, serviceProvider.get());
                    createSubscriptionTransaction(subscription, invoice, updatedTransaction, serviceProvider.get());
                }

                // 3. ServiceProviderWallet
                createOrUpdateServiceProviderWallet(invoice, updatedTransaction, serviceProvider.get());

                // 4. CouponUsageTracker
                createOrUpdateCouponUsageTracker(invoice, updatedTransaction, serviceProvider.get());

                // 5. DiscountUsageTracker
                createOrUpdateDiscountUsageTracker(invoice, updatedTransaction, serviceProvider.get());

            }
        } else {
            log.warn("Transaction not found for verification with txnRef: {}", txnRef);
        }
    }

    private void createOrUpdateDiscountUsageTracker(Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider) {
        discountUsageTrackerService.createOrUpdateDiscountUsageTracker(invoice, updatedTransaction, serviceProvider);
    }

    private void createOrUpdateCouponUsageTracker(Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider) {
        couponUsageTrackerService.createOrUpdateCouponUsageTracker(invoice, updatedTransaction, serviceProvider);
    }

    private void createOrUpdateServiceProviderWallet(Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider) {
        walletService.createOrUpdateServiceProviderWallet(invoice, updatedTransaction, serviceProvider);
    }

    private void createSubscriptionTransaction(ServiceProviderSubscription subscription, Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider) {
        subscriptionTransactionService.createSubscriptionTransaction(subscription, invoice, updatedTransaction, serviceProvider);
    }

    private ServiceProviderSubscription createOrUpdateServiceProviderSubscription(Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider) {
        return subscriptionService.createOrUpdateServiceProviderSubscription(invoice, updatedTransaction, serviceProvider);
    }

    private void createAddOnTransaction(Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider) {
        addOnTransactionService.createAddOnTransaction(invoice, updatedTransaction, serviceProvider);
    }

    private void createOrUpdatePayGoCredit(Invoice invoice, Transaction updatedTransaction, ServiceProvider serviceProvider) {
        payGoCreditService.createOrUpdatePayGoCredit(invoice, updatedTransaction, serviceProvider);
    }

    @Transactional
    public void processForFailure(Map<String, Object> body, String gatewayName) {
        String txnRef = "";
        if (gatewayName.equalsIgnoreCase("chapa")) {
            Map<String, Object> payload = (Map<String, Object>) body.get("data");
            txnRef = (String) payload.get("tx_ref");
        } else if (gatewayName.equalsIgnoreCase("pesapal")) {
            Map<String, Object> txnDetails = (Map<String, Object>) body.get("txnDetails");
            txnRef = (String) txnDetails.get("orderMerchantReference");
        } else {
            log.warn("Unsupported gateway for failure processing: {}", gatewayName);
            return;
        }

        Optional<Transaction> transaction = transactionService.getTransactionByTxnRef(txnRef);

        if (transaction.isPresent()) {
            System.out.println("======================================================> ZZZZ Transaction Reference: " + txnRef);
            Transaction txn = transaction.get();
            txn.setIsTxnVerified(false);
            txn.setTxnVerificationResponseMap(body);
            txn.setStatus(TransactionStatusEnum.FAILED);

            log.info("Updating transaction for failed verification: {}", txnRef);
            Transaction updatedTransaction = transactionService.saveTransaction(txn);
        } else {
            log.warn("Transaction is not found for verification with txnRef: {}", txnRef);
        }
    }
}
