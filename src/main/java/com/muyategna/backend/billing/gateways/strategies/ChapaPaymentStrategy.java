package com.muyategna.backend.billing.gateways.strategies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.PaymentMethod;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.billing.enums.AccountingTypeEnum;
import com.muyategna.backend.billing.enums.TransactionStatusEnum;
import com.muyategna.backend.billing.enums.TransactionTypeEnum;
import com.muyategna.backend.billing.enums.UserEntityType;
import com.muyategna.backend.billing.gateways.dto.PaymentRequirement;
import com.muyategna.backend.billing.gateways.service.PaymentServiceUtil;
import com.muyategna.backend.billing.service.InvoiceService;
import com.muyategna.backend.billing.service.PaymentMethodService;
import com.muyategna.backend.billing.service.TransactionService;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.service.ServiceProviderServiceInterface;
import com.muyategna.backend.user.entity.UserProfile;
import com.muyategna.backend.user.service.UserProfileService;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;

@Component
@Slf4j
public class ChapaPaymentStrategy implements PaymentStrategy {

    @Value("${chapa.base-url}")
    private String chapaBaseUrl;

    @Value("${chapa.secret-key}")
    private String secretKey;

    @Value("${chapa.secret-hash}")
    private String chapaWebhookSecret;

    @Value("${chapa.callback-url}")
    private String callbackUrl;

    @Value("${chapa.return-url}")
    private String returnUrl;

    @Value("${chapa.customization-title}")
    private String customizationTitle;

    @Value("${chapa.payment-initialize-path}")
    private String chapaPaymentInitializePath;

    @Value("${chapa.verify-payment-path}")
    private String chapaVerifyPaymentPath;

    private final RestTemplate restTemplate = new RestTemplate();

    private final InvoiceService invoiceService;
    private final TransactionService transactionService;
    private final PaymentMethodService paymentMethodService;
    private final ServiceProviderServiceInterface serviceProviderService;
    private final UserProfileService userProfileService;
    private final PaymentServiceUtil paymentServiceUtil;

    public ChapaPaymentStrategy(InvoiceService invoiceService, TransactionService transactionService, PaymentMethodService paymentMethodService, ServiceProviderServiceInterface serviceProviderService, UserProfileService userProfileService, PaymentServiceUtil paymentServiceUtil) {
        this.invoiceService = invoiceService;
        this.transactionService = transactionService;
        this.paymentMethodService = paymentMethodService;
        this.serviceProviderService = serviceProviderService;
        this.userProfileService = userProfileService;
        this.paymentServiceUtil = paymentServiceUtil;
    }

    @Transactional
    @Override
    public Map<String, Object> initializePayment(PaymentRequirement paymentRequirement) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Initializing payment with Chapa <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        Long paymentMethodId = paymentRequirement.getPaymentMethodId();
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
                TransactionStatusEnum.INITIATED,
                paymentMethod,
                TransactionTypeEnum.PAYOUT
        );
        if (transaction == null) {
            throw new IllegalStateException("Failed to create transaction");
        }

        log.info("Initialized invoice ID: {}, transaction ID: {}", invoice.getId(), transaction.getId());

        // Prepare Chapa request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = prepareChapaRequestBody(transaction, serviceProvider, userProfile);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = null;
        try {
            response = restTemplate.postForEntity(chapaBaseUrl + chapaPaymentInitializePath, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (Map<String, Object>) response.getBody().get("data");
            } else {
                paymentServiceUtil.processForFailure(response.getBody(), "chapa");
                log.error("Chapa initialization failed for transaction ID {}: {}", transaction.getId(), response.getStatusCode());

                return response.getBody();
            }
        } catch (RestClientException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            paymentServiceUtil.processForFailure(errorResponse, "chapa");
            log.error("Chapa payment initialization error for transaction ID {}: {}", transaction.getId(), e.getMessage());

            throw new RuntimeException("Failed to initialize payment with Chapa for transaction ID " + transaction.getId());
        }

    }


    @NotNull
    private Map<String, Object> prepareChapaRequestBody(Transaction transaction, ServiceProvider serviceProvider, UserProfile userProfile) {
        Map<String, Object> body = new HashMap<>();

        body.put("amount", transaction.getAmountPaid());
        body.put("currency", transaction.getCurrency());
        body.put("tx_ref", transaction.getTxnRef());
        body.put("callback_url", callbackUrl);
        body.put("return_url", returnUrl);
        body.put("customization_title", customizationTitle);

        if (serviceProvider != null) {
            body.put("email", serviceProvider.getBusinessEmail());
            body.put("first_name", serviceProvider.getBusinessName());
            body.put("last_name", serviceProvider.getBusinessName());
        } else if (userProfile != null) {
            body.put("email", userProfile.getEmail());
            body.put("first_name", userProfile.getFirstName());
            body.put("last_name", userProfile.getLastName());
        }
        return body;
    }

    @Override
    public void processWebhook(Map<String, Object> payload) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Processing webhook from Chapa <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        String chapaSignature = (String) payload.get("chapa-signature");
        String xChapaSignature = (String) payload.get("x-chapa-signature");
        String responseBody = (String) payload.get("responseBody");

        if (payload.isEmpty() && chapaSignature == null && xChapaSignature == null) {
            throw new BadRequestException("Invalid Chapa webhook payload");
        }

        String computedHash = hmacSha256(responseBody, chapaWebhookSecret);

        if (!computedHash.equals(chapaSignature) && !computedHash.equals(xChapaSignature)) {
            throw new BadRequestException("Invalid signature for Chapa webhook");
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> payloadMap = objectMapper.readValue(responseBody, new TypeReference<>() {
            });

            // 2. Extract transaction reference & status
            String txRef = (String) payloadMap.get("tx_ref");
            String status = (String) payloadMap.get("status");

            if (txRef == null || status == null) {
                throw new IllegalArgumentException("Invalid Chapa webhook payload");
            }

            // 4. Update db if payment was successful
            if ("success".equalsIgnoreCase(status)) {
                // e.g., subscriptionService.activateSubscription(userIdFromTxRef, expiryDate);
                paymentServiceUtil.processForWebhook(payloadMap, "chapa");

                // 3. Double-check payment with Chapa verify API
                Map<String, Object> verificationParams = new HashMap<>();
                verificationParams.put("txRef", txRef);
                boolean verified = verifyTransaction(verificationParams);
                if (!verified) {
                    throw new BadRequestException("Payment verification failed for tx_ref: " + txRef);
                }
                log.info("Payment verified for tx_ref: {}", txRef);

            } else {
                log.info("Payment failed or is pending for tx_ref: {}", txRef);
                paymentServiceUtil.processForWebhook(payloadMap, "chapa");
            }
        } catch (IOException e) {
            log.error("Error processing Chapa webhook payload: {}", e.getMessage());
            throw new BadRequestException("Invalid Chapa webhook payload format");
        } catch (IllegalArgumentException e) {
            log.error("Error processing Chapa webhook: {}", e.getMessage());
            throw new BadRequestException("Invalid Chapa webhook payload");
        } catch (Exception e) {
            log.error("Unexpected error processing Chapa webhook: {}", e.getMessage());
            throw new RuntimeException("Unexpected error processing Chapa webhook", e);
        }
    }

    /**
     * Calls Chapa's /transaction/verify/{tx_ref} endpoint to confirm payment status.
     */
    @Override
    public boolean verifyTransaction(Map<String, Object> params) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Verifying payment with Chapa <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        String txRef = (String) params.get("txRef");
        try {
            String url = chapaBaseUrl + chapaVerifyPaymentPath + txRef;

            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "Bearer " + secretKey);
            headers.setBearerAuth(secretKey);

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, Object>>() {
            });

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> body = response.getBody();//.get("data");
                System.out.println("======================================================> VERIFICATION Response data: " + body);

                Map<String, Object> data = (Map<String, Object>) body.get("data");
                String paymentStatus = (String) data.get("status");
                boolean verified = "success".equalsIgnoreCase(paymentStatus);
                if (verified) {
                    paymentServiceUtil.processForVerificationSuccess(body, "chapa");
                    return true;
                } else {
                    log.warn("Payment verification failed for tx_ref: {} with status: {}", txRef, paymentStatus);
                    paymentServiceUtil.processForFailure(body, "chapa");
                    return false;
                }

            }
        } catch (Exception e) {
            log.error("Error verifying payment with Chapa: {}", e.getMessage());
            Map<String, Object> body = new HashMap<>();
            body.put("error", e.getMessage());
            Map<String, Object> data = new HashMap<>();

            data.put("tx_ref", txRef);
            body.put("data", data);
            paymentServiceUtil.processForFailure(body, "chapa");
        }
        return false;
    }


    private String hmacSha256(String data, String secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error while verifying Chapa signature", e);
        }
    }


}


