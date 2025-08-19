//package com.muyategna.backend.billing.gateways.service;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.muyategna.backend.billing.gateways.dto.ChapaPaymentRequest;
//import jakarta.ws.rs.BadRequestException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.HexFormat;
//import java.util.Map;
//import java.util.UUID;
//
//@Service
//@Slf4j
//public class ChapaServiceImpl implements ChapaService {
//
//    @Value("${chapa.secret-key}")
//    private String secretKey;
//
//    @Value("${chapa.base-url}")
//    private String baseUrl;
//
//    @Value("${chapa.secret-hash}")
//    private String chapaWebhookSecret;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    @Override
//    public String initializePayment(ChapaPaymentRequest request) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + secretKey);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        String txRef = UUID.randomUUID().toString();
//        Map<String, Object> body = new HashMap<>();
//        body.put("amount", request.getAmount());
//        body.put("currency", "ETB");
//        body.put("email", request.getEmail());
//        body.put("first_name", request.getFirstName());
//        body.put("last_name", request.getLastName());
//        body.put("tx_ref", txRef);
//        body.put("callback_url", request.getCallbackUrl());
//        body.put("return_url", request.getReturnUrl());
//        body.put("customization_title", request.getCustomizationTitle());
//        body.put("customization_description", request.getCustomizationDescription());
//
//        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
//
//        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl + "/transaction/initialize", entity, Map.class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
//            return (String) data.get("checkout_url");
//        }
//
//        throw new RuntimeException("Failed to initialize payment with Chapa");
//    }
//
//    @Override
//    public boolean verifyTransaction(String txRef) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + secretKey);
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<Map> response = restTemplate.exchange(
//                baseUrl + "/transaction/verify/" + txRef,
//                HttpMethod.GET,
//                entity,
//                Map.class
//        );
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
//            return "success".equalsIgnoreCase((String) data.get("status"));
//        }
//
//        return false;
//    }
//
//
//    @Override
//    public void handleWebhook(String payload, String chapaSignature, String xChapaSignature) {
//
//        if ((payload == null || payload.isEmpty()) && chapaSignature == null && xChapaSignature == null) {
//            throw new BadRequestException("Invalid Chapa webhook payload");
//        }
//
//        String computedHash = hmacSha256(payload, chapaWebhookSecret);
//
//        if (!computedHash.equals(chapaSignature) && !computedHash.equals(xChapaSignature)) {
//            throw new BadRequestException("Invalid signature for Chapa webhook");
//        }
//
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            Map<String, Object> payloadMap = objectMapper.readValue(payload, new TypeReference<>() {
//            });
//
//            // 2. Extract transaction reference & status
//            String txRef = (String) payloadMap.get("tx_ref");
//            String status = (String) payloadMap.get("status");
//
//            System.out.println("=================================================================== tx_ref: " + txRef);
//            System.out.println("=================================================================== status: " + status);
//
//
//            if (txRef == null || status == null) {
//                throw new IllegalArgumentException("Invalid Chapa webhook payload");
//            }
//
//            // 3. Double-check payment with Chapa verify API
//            boolean verified = verifyTransactionWithChapa(txRef);
//            if (!verified) {
//                throw new BadRequestException("Payment verification failed for tx_ref: " + txRef);
//            }
//
//            // 4. Update subscription if payment was successful
//            if ("success".equalsIgnoreCase(status)) {
//                // e.g., subscriptionService.activateSubscription(userIdFromTxRef, expiryDate);
//                System.out.println("Payment verified for tx_ref: " + txRef);
//            } else {
//                System.out.println("Payment failed or is pending for tx_ref: " + txRef);
//            }
//        } catch (IOException e) {
//            log.error("Error processing Chapa webhook payload: {}", e.getMessage());
//            throw new BadRequestException("Invalid Chapa webhook payload format");
//        } catch (IllegalArgumentException e) {
//            log.error("Error processing Chapa webhook: {}", e.getMessage());
//            throw new BadRequestException("Invalid Chapa webhook payload");
//        } catch (Exception e) {
//            log.error("Unexpected error processing Chapa webhook: {}", e.getMessage());
//            throw new RuntimeException("Unexpected error processing Chapa webhook", e);
//        }
//    }
//
//    /**
//     * Calls Chapa's /transaction/verify/{tx_ref} endpoint to confirm payment status.
//     */
//    private boolean verifyTransactionWithChapa(String txRef) {
//        try {
//            String url = baseUrl + "/transaction/verify/" + txRef;
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "Bearer " + secretKey);
//
//            HttpEntity<Void> entity = new HttpEntity<>(headers);
//            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
//
//            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//                Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
//                if (data != null) {
//                    String paymentStatus = (String) data.get("status");
//                    return "success".equalsIgnoreCase(paymentStatus);
//                }
//            }
//        } catch (Exception e) {
//            log.error("Error verifying payment with Chapa: {}", e.getMessage());
//        }
//        return false;
//    }
//
//    private String hmacSha256(String data, String secret) {
//        try {
//            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
//            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//            sha256_HMAC.init(secret_key);
//            byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
//            return HexFormat.of().formatHex(hash);
//        } catch (Exception e) {
//            throw new RuntimeException("Error while verifying Chapa signature", e);
//        }
//    }
//}
