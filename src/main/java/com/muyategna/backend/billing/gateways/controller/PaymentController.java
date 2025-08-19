package com.muyategna.backend.billing.gateways.controller;

import com.muyategna.backend.billing.gateways.dto.PaymentRequirement;
import com.muyategna.backend.billing.gateways.dto.bank_transfer_transaction.BankTransferTransactionCreateDto;
import com.muyategna.backend.billing.gateways.service.PaymentService;
import com.muyategna.backend.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/secured/billing/payment")
@Tag(name = "Payment Controller", description = "Payment APIs for Chapa Gateway")
public class PaymentController {

    //    private final ChapaService chapaService;
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
//        this.chapaService = chapaService;
        this.paymentService = paymentService;
    }

    @PostMapping("/initialize")
    @Operation(summary = "Initialize payment", description = "Initialize payment")
    public ResponseEntity<ApiResponse<Map<String, Object>>> initialize(
            @Parameter(description = "Payment Requirement") @RequestBody @Valid PaymentRequirement paymentRequirement,
            HttpServletRequest request) {

        Map<String, Object> paymentResponse = paymentService.initializePayment(paymentRequirement);

        ApiResponse<Map<String, Object>> response = ApiResponse.<Map<String, Object>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Checkout URL retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(paymentResponse)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/pesapal/IPN")
    @Operation(summary = "Handle Pesapal IPN", description = "Handle Pesapal IPN")
    public ResponseEntity<ApiResponse<Void>> pesapalIPN(
            @RequestParam("OrderTrackingId") String orderTrackingId,
            @RequestParam("OrderNotificationType") String orderNotificationType,
            @RequestParam("OrderMerchantReference") String orderMerchantReference,
            HttpServletRequest request) {

        System.out.println("======================================================>>> Received Pesapal IPN: " +
                "OrderTrackingId=" + orderTrackingId +
                ", OrderNotificationType=" + orderNotificationType +
                ", OrderMerchantReference=" + orderMerchantReference);

        Map<String, Object> payload = new HashMap<>();
        payload.put("orderTrackingId", orderTrackingId);
        payload.put("orderNotificationType", orderNotificationType);
        payload.put("orderMerchantReference", orderMerchantReference);

        paymentService.processWebhook(payload);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Pesapal IPN received successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/chapa/webhook")
    @Operation(summary = "Handle Chapa webhook", description = "Handle Chapa webhook")
    public ResponseEntity<ApiResponse<Void>> handleWebhook(
            @Parameter(description = "Webhook payload") @RequestBody String responseBody,
            @Parameter(description = "chapa-signature") @RequestHeader(value = "chapa-signature", required = false) String chapaSignature,
            @Parameter(description = "x-chapa-signature") @RequestHeader(value = "x-chapa-signature", required = false) String xChapaSignature,
            HttpServletRequest request) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("chapa-signature", chapaSignature);
        payload.put("x-chapa-signature", xChapaSignature);
        payload.put("responseBody", responseBody);


        paymentService.processWebhook(payload);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Webhook received successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // BANK TRANSFER PAYMENT STRATEGY
    @PostMapping(path = "/bank-transfer/report-payment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Report Bank Transfer Payment", description = "Report Bank Transfer Payment")
    public ResponseEntity<ApiResponse<Void>> reportBankTransferPayment(
            @Parameter(description = "Bank Transfer Payment Object") @ModelAttribute @Valid BankTransferTransactionCreateDto bankTransferDto,
            HttpServletRequest request) {

        paymentService.reportBankTransferPayment(bankTransferDto);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.CREATED.value())
                .success(true)
                .message("Bank Transfer Payment reported successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping("/bank-transfer/{txnRef}/verify")
    @Operation(summary = "Verify Bank Transfer Payment", description = "Verify Bank Transfer Payment")
    public ResponseEntity<ApiResponse<Void>> verifyBankTransferPayment(
            @Parameter(description = "Bank transfer transaction reference") @PathVariable String txnRef,
            HttpServletRequest request) {

        paymentService.verifyBankTransferTransaction(txnRef);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Bank Transfer Payment verified successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PutMapping("/bank-transfer/{txnRef}/cancel")
    @Operation(summary = "Cancel Bank Transfer Payment", description = "Cancel Bank Transfer Payment")
    public ResponseEntity<ApiResponse<Void>> cancelBankTransferPayment(
            @Parameter(description = "Bank transfer transaction reference") @PathVariable String txnRef,
            HttpServletRequest request) {

        paymentService.cancelBankTransferTransaction(txnRef);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Bank Transfer Payment cancelled successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
