package com.muyategna.backend.billing.controller;

import com.muyategna.backend.billing.assembler.CouponDtoModelAssembler;
import com.muyategna.backend.billing.dto.coupon.CouponDto;
import com.muyategna.backend.billing.service.CouponService;
import com.muyategna.backend.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/public/billing/coupons")
@Tag(name = "Coupon Public Controller")
public class CouponPublicController {
    private final CouponService couponService;
    private final CouponDtoModelAssembler assembler;

    @Autowired
    public CouponPublicController(CouponService couponService, CouponDtoModelAssembler assembler) {
        this.couponService = couponService;
        this.assembler = assembler;
    }

    @GetMapping("/{couponId}")
    @Operation(summary = "Get Coupon by Id", description = "Get a coupon by its id")
    public ResponseEntity<ApiResponse<EntityModel<CouponDto>>> getCouponById(
            @Parameter(description = "Id of the coupon", required = true) @PathVariable Long couponId,
            HttpServletRequest request) {
        EntityModel<CouponDto> entityModel = assembler.toModel(couponService.getCouponById(couponId), request);
        ApiResponse<EntityModel<CouponDto>> response = ApiResponse.<EntityModel<CouponDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("All coupons retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping
    @Operation(summary = "Get Coupon by code", description = "Get a coupon by its code")
    public ResponseEntity<ApiResponse<EntityModel<CouponDto>>> getCouponByCode(
            @Parameter(description = "Coupon code", required = true) @RequestParam String couponCode,
            HttpServletRequest request) {
        EntityModel<CouponDto> entityModel = assembler.toModel(couponService.getCouponByCode(couponCode), request);
        ApiResponse<EntityModel<CouponDto>> response = ApiResponse.<EntityModel<CouponDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("All coupons retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-global-coupon")
    @Operation(summary = "Get global coupon", description = "Get global coupon")
    public ResponseEntity<ApiResponse<EntityModel<CouponDto>>> getGlobalCoupon(
            @Parameter(description = "Discount plan id", required = true) @RequestParam(required = false) Long discountPlanId,
            HttpServletRequest request
    ) {
        EntityModel<CouponDto> entityModel = assembler.toModel(couponService.getGlobalCouponForDiscountPlan(discountPlanId), request);
        ApiResponse<EntityModel<CouponDto>> response = ApiResponse.<EntityModel<CouponDto>>builder()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Global coupon retrieved successfully")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(entityModel)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
