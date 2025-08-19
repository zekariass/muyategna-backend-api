package com.muyategna.backend.billing.gateways.strategies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muyategna.backend.billing.entity.Invoice;
import com.muyategna.backend.billing.entity.PaymentMethod;
import com.muyategna.backend.billing.entity.Transaction;
import com.muyategna.backend.billing.enums.*;
import com.muyategna.backend.billing.gateways.dto.BillingAddress;
import com.muyategna.backend.billing.gateways.dto.PaymentRequirement;
import com.muyategna.backend.billing.gateways.dto.PesapalPaymentRequest;
import com.muyategna.backend.billing.gateways.service.PaymentServiceUtil;
import com.muyategna.backend.billing.gateways.service.PesapalTokenService;
import com.muyategna.backend.billing.service.InvoiceService;
import com.muyategna.backend.billing.service.PaymentMethodService;
import com.muyategna.backend.billing.service.TransactionService;
import com.muyategna.backend.location.entity.Address;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.service.ServiceProviderServiceInterface;
import com.muyategna.backend.user.entity.UserProfile;
import com.muyategna.backend.user.service.UserProfileService;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class PesapalPaymentStrategy implements PaymentStrategy {

    @Value("${pesapal.consumer-key}")
    private String consumerKey;

    @Value("${pesapal.consumer-secret}")
    private String consumerSecret;

    @Value("${pesapal.base-url}")
    private String pesapalBaseUrl;

    @Value("${pesapal.auth-path}")
    private String pesapalAuthPath;

    @Value("${pesapal.payment-request-path}")
    private String pesapalPaymentRequestPath;

    @Value("${pesapal.notification-id}")
    private String notificationId;

    @Value("${pesapal.callback-url}")
    private String callbackUrl;

    @Value("${pesapal.cancellation-url}")
    private String cancellationUrl;

    @Value("${pesapal.transaction-status-path}")
    private String txnStatusPath;


    private final PaymentMethodService paymentMethodService;
    private final InvoiceService invoiceService;
    private final TransactionService transactionService;
    private final PaymentServiceUtil paymentServiceUtil;
    private final ServiceProviderServiceInterface serviceProviderService;
    private final UserProfileService userProfileService;
    private final PesapalTokenService pesapalTokenService;

    @Autowired
    public PesapalPaymentStrategy(PaymentMethodService paymentMethodService,
                                  InvoiceService invoiceService,
                                  TransactionService transactionService,
                                  PaymentServiceUtil paymentServiceUtil,
                                  ServiceProviderServiceInterface serviceProviderService,
                                  UserProfileService userProfileService, PesapalTokenService pesapalTokenService) {
        this.paymentMethodService = paymentMethodService;
        this.invoiceService = invoiceService;
        this.transactionService = transactionService;
        this.paymentServiceUtil = paymentServiceUtil;
        this.serviceProviderService = serviceProviderService;
        this.userProfileService = userProfileService;
        this.pesapalTokenService = pesapalTokenService;
    }

    @Override
    public Map<String, Object> initializePayment(PaymentRequirement paymentRequirement) {
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

        // Authenticate and process the payment using Pesapal API
        String accessToken = getPesapalAccessToken();

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


        // Submit order request to Pesapal
        return (Map<String, Object>) submitOrderRequest(accessToken, invoice, transaction, serviceProvider, userProfile);
    }


    @Override
    public void processWebhook(Map<String, Object> payload) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Processing webhook from Chapa <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        if (payload.isEmpty()) {
            throw new BadRequestException("Invalid pesapal webhook payload");
        }

        String orderTrackingId = (String) payload.get("orderTrackingId");
        String orderNotificationType = (String) payload.get("orderNotificationType");
        String orderMerchantReference = (String) payload.get("orderMerchantReference");


        try {

            if (orderTrackingId == null || !Objects.equals(orderNotificationType, "IPNCHANGE") || orderMerchantReference == null) {
                paymentServiceUtil.processForWebhook(payload, "pesapal");
                throw new IllegalArgumentException("Invalid Chapa webhook payload");
            }

            paymentServiceUtil.processForWebhook(payload, "pesapal");

            boolean verified = verifyTransaction(payload);
            if (!verified) {
                throw new BadRequestException("Transaction status check failed for orderTrackingId: " + orderTrackingId);
            }

            log.info("Transaction status verified for orderTrackingId: {}", orderTrackingId);

        } catch (IllegalArgumentException e) {
            log.error("Error processing Chapa webhook: {}", e.getMessage());
            throw new BadRequestException("Invalid Chapa webhook payload");
        } catch (Exception e) {
            log.error("Unexpected error processing Chapa webhook: {}", e.getMessage());
            throw new RuntimeException("Unexpected error processing Chapa webhook", e);
        }
    }

    private Map submitOrderRequest(String accessToken, Invoice invoice, Transaction transaction, ServiceProvider serviceProvider, UserProfile userProfile) {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        // Prepare request body
        PesapalPaymentRequest pesapalPaymentRequest = preparePesapalPaymentRequest(transaction, serviceProvider, userProfile);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(pesapalPaymentRequest);

            // Make the request to Pesapal API to submit order
            HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    pesapalBaseUrl.trim() + pesapalPaymentRequestPath,
                    HttpMethod.POST,
                    entity,
                    Map.class);

            System.out.println("======================================================> Response: " + response.getBody());

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                paymentServiceUtil.processForFailure(response.getBody(), "pesapal");
                throw new RuntimeException("Failed to submit order request: " + response);
            }
        } catch (JsonProcessingException e) {
            processFailure(e);

            log.error("Error converting PesapalPaymentRequest to JSON", e);
            throw new RuntimeException("Failed to convert payment request to JSON", e);
        } catch (Exception e) {
            processFailure(e);
            log.error("Unexpected error", e);
            throw new RuntimeException("Unexpected error occurred while processing payment request", e);
        }
    }

    private void processFailure(Exception e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        paymentServiceUtil.processForFailure(errorResponse, "pesapal");
    }

    @NotNull
    private PesapalPaymentRequest preparePesapalPaymentRequest(Transaction transaction,
                                                               ServiceProvider serviceProvider,
                                                               UserProfile userProfile) {
        PesapalPaymentRequest pesapalPaymentRequest = new PesapalPaymentRequest();

        // Generate a unique ID for the payment request
        System.out.println("======================================================> txnRef: " + transaction.getTxnRef());

        pesapalPaymentRequest.setId(transaction.getTxnRef());
        pesapalPaymentRequest.setCurrency(transaction.getCurrency());
        pesapalPaymentRequest.setAmount(transaction.getAmountPaid());
        pesapalPaymentRequest.setDescription(transaction.getDescription());
        pesapalPaymentRequest.setRedirect_mode("TOP_WINDOW");
        pesapalPaymentRequest.setCallback_url(callbackUrl);
        pesapalPaymentRequest.setCancellation_url(cancellationUrl);
        pesapalPaymentRequest.setNotification_id(notificationId.trim());

        // Create billing address

        BillingAddress billingAddress = prepareBillingAddress(serviceProvider, userProfile);

        pesapalPaymentRequest.setBilling_address(billingAddress);
        return pesapalPaymentRequest;
    }

    @NotNull
    private BillingAddress prepareBillingAddress(ServiceProvider serviceProvider, UserProfile userProfile) {
        BillingAddress billingAddress = new BillingAddress();
        if (serviceProvider != null) {
            Address providerAddress = serviceProvider.getBusinessAddress();
            billingAddress.setCity(providerAddress.getCity().getName());
            billingAddress.setFirst_name(serviceProvider.getBusinessName());
            billingAddress.setPhone_number(serviceProvider.getBusinessPhone());
            billingAddress.setEmail_address(serviceProvider.getBusinessEmail());

//            billingAddress.setMiddle_name(serviceProvider.getBusinessName());
//            billingAddress.setLast_name(serviceProvider.getBusinessName());
//            billingAddress.setPostal_code(providerAddress.getPostalCode());
//            billingAddress.setState("Kampala");
//            billingAddress.setZip_code(12345);
//            billingAddress.setCountry_code(serviceProvider.getBusinessAddress().getCountry().getCountryIsoCode2());
//            billingAddress.setLine_1("123 Main St");
//            billingAddress.setLine_2("Apt 4B");
        } else if (userProfile != null) {
            billingAddress.setFirst_name(userProfile.getFirstName());
            billingAddress.setMiddle_name(userProfile.getMiddleName());
            billingAddress.setLast_name(userProfile.getLastName());
            billingAddress.setPhone_number(userProfile.getPhoneNumber());
            billingAddress.setEmail_address(userProfile.getEmail());

//            billingAddress.setCity("Kampala");
//            billingAddress.setPostal_code(12345);
//            billingAddress.setState("Kampala");
//            billingAddress.setZip_code(12345);
//            billingAddress.setCountry_code("UG");
//            billingAddress.setLine_1("123 Main St");
//            billingAddress.setLine_2("Apt 4B");
        }

        return billingAddress;
    }

    /**
     * Retrieves the Pesapal access token using the consumer key and secret.
     *
     * @return The access token as a String.
     * @throws RuntimeException if the request fails or the response is not as expected.
     */
    private String getPesapalAccessToken() {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = """
                {
                  "consumer_key": "%s",
                  "consumer_secret": "%s"
                }
                """.formatted(consumerKey.trim(), consumerSecret.trim());

        // Make the request to Pesapal API to get access token
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                pesapalBaseUrl.trim() + pesapalAuthPath.trim(),
                HttpMethod.POST,
                entity,
                Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            String status = (String) response.getBody().get("status");
            if (!Objects.equals(status, "200")) {
                throw new RuntimeException("Failed to get Pesapal access token: " + response.getBody().get("error"));
            }

            // Save the token in Redis for future use
            String token = response.getBody().get("token").toString();
            pesapalTokenService.saveToken(token);
            return token;
        } else {
            throw new RuntimeException("Failed to get Pesapal access token: " + response);
        }
    }

    @Override
    public boolean verifyTransaction(Map<String, Object> params) {
        // Get the access token from Redis or fetch a new one if not available
        String orderTrackingId = (String) params.get("orderTrackingId");

        String token = pesapalTokenService.getToken();
        if (token == null) {
            token = getPesapalAccessToken();
        }

        RestTemplate restTemplate = new RestTemplate();
        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        // Make the request to Pesapal API to verify transaction
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                pesapalBaseUrl.trim() + txnStatusPath.trim() + "?orderTrackingId=" + orderTrackingId.trim(),
                HttpMethod.GET,
                entity,
                Map.class);

        System.out.println("======================================================> PESAPAL STATUS RESPONSE: " + response);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {

            String status = (String) response.getBody().get("status");
            Map<String, Object> body = (Map<String, Object>) response.getBody();
            if (Objects.equals(status, "200")) {
                paymentServiceUtil.processForVerificationSuccess(body, "pesapal");
                log.info("Payment verification successful for orderTrackingId: {}", orderTrackingId);
                return true;
            } else {
                log.warn("Payment verification failed for orderTrackingId: {} with status: {}", orderTrackingId, status);

                // Add transaction info to body for failure processing
                body.put("txnDetails", params);
                paymentServiceUtil.processForFailure(body, "pesapal");
                return false;
            }
        }
        return false;
    }
}
