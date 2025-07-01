package com.muyategna.backend.billing.controller;

import com.muyategna.backend.billing.assembler.PaymentMethodLocalizedDtoModelPublicAssembler;
import com.muyategna.backend.billing.dto.payment_method.PaymentMethodLocalizedDto;
import com.muyategna.backend.billing.service.PaymentMethodService;
import com.muyategna.backend.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/public/billing/payment-methods")
@Tag(name = "Payment Methods API", description = "Payment Methods API")
public class PaymentMethodPublicController {

    private final PaymentMethodService paymentMethodService;
    private final PaymentMethodLocalizedDtoModelPublicAssembler assembler;

    @Autowired
    public PaymentMethodPublicController(PaymentMethodService paymentMethodService, PaymentMethodLocalizedDtoModelPublicAssembler assembler) {
        this.paymentMethodService = paymentMethodService;
        this.assembler = assembler;
    }

    @GetMapping("/{paymentMethodId}")
    @Operation(summary = "Get payment method by id", description = "Get payment method by id")
    public ResponseEntity<ApiResponse<EntityModel<PaymentMethodLocalizedDto>>> getPaymentMethodById(@PathVariable Long paymentMethodId, HttpServletRequest request) {
        PaymentMethodLocalizedDto paymentMethod = paymentMethodService.getPaymentMethodById(paymentMethodId);

        EntityModel<PaymentMethodLocalizedDto> paymentMethodModel = assembler.toModel(paymentMethod, request);

        ApiResponse<EntityModel<PaymentMethodLocalizedDto>> response = ApiResponse.<EntityModel<PaymentMethodLocalizedDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Payment Method with id: %d retrieved successfully".formatted(paymentMethodId))
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(paymentMethodModel)
                .build();
        return ResponseEntity.ok(response);

    }


    @GetMapping
    @Operation(summary = "Get all payment method for current country", description = "Get all payment method for current country")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<PaymentMethodLocalizedDto>>>> getAllPaymentMethodsForCurrentCountry(HttpServletRequest request) {
        List<PaymentMethodLocalizedDto> paymentMethods = paymentMethodService.getAllPaymentMethodsForCurrentCountry();
        CollectionModel<EntityModel<PaymentMethodLocalizedDto>> collectionModel = assembler.toCollectionModel(paymentMethods, request);

        ApiResponse<CollectionModel<EntityModel<PaymentMethodLocalizedDto>>> response = ApiResponse.<CollectionModel<EntityModel<PaymentMethodLocalizedDto>>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Payment Methods retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(collectionModel)
                .build();
        return ResponseEntity.ok(response);

    }
}
