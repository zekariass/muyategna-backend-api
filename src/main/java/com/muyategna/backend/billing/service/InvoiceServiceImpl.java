package com.muyategna.backend.billing.service;

import com.muyategna.backend.billing.entity.*;
import com.muyategna.backend.billing.enums.InvoiceStatusEnum;
import com.muyategna.backend.billing.enums.ProductType;
import com.muyategna.backend.billing.enums.UserEntityType;
import com.muyategna.backend.billing.gateways.dto.PaymentRequirement;
import com.muyategna.backend.billing.mapper.AddOnPlanMapper;
import com.muyategna.backend.billing.mapper.SubscriptionPlanMapper;
import com.muyategna.backend.billing.repository.InvoiceRepository;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.location.entity.Country;
import com.muyategna.backend.location.mapper.CountryMapper;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final AddOnPlanService addOnPlanService;
    private final SubscriptionPlanService subscriptionPlanService;
    private final DiscountPlanService discountPlanService;
    private final CouponService couponService;
    private final TaxRuleService taxRuleService;
    private final SequenceNumberGenerator sequenceNumberGenerator;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              AddOnPlanService addOnPlanService,
                              SubscriptionPlanService subscriptionPlanService,
                              DiscountPlanService discountPlanService,
                              CouponService couponService,
                              TaxRuleService taxRuleService,
                              SequenceNumberGenerator sequenceNumberGenerator) {
        this.invoiceRepository = invoiceRepository;
        this.addOnPlanService = addOnPlanService;
        this.subscriptionPlanService = subscriptionPlanService;
        this.discountPlanService = discountPlanService;
        this.couponService = couponService;
        this.taxRuleService = taxRuleService;
        this.sequenceNumberGenerator = sequenceNumberGenerator;
    }

    @Transactional
    @Override
    public Invoice createInvoice(PaymentRequirement paymentRequirement) {
        // Extract common fields
        ProductType productType = paymentRequirement.getProductType();
        Long addOnPlanId = paymentRequirement.getAddOnPlanId();
        Long subscriptionPlanId = paymentRequirement.getSubscriptionPlanId();
        Long discountPlanId = paymentRequirement.getDiscountPlanId();
        String couponCode = paymentRequirement.getCouponCode();
        UserEntityType userEntityType = paymentRequirement.getUserEntityType();
        Long userEntityId = paymentRequirement.getUserEntityId();

        // Get base amount
        BigDecimal amount = getBaseAmount(productType, addOnPlanId, subscriptionPlanId);
        BigDecimal totalAmount = amount;

        // Validate discount plan and coupon
        Optional<DiscountPlan> discountPlan = Optional.ofNullable(discountPlanId)
                .flatMap(id -> discountPlanService.getAndValidateDiscountPlan(id, paymentRequirement));

        Optional<Coupon> coupon = Optional.ofNullable(couponCode)
                .flatMap(code -> couponService.getAndValidateCoupon(code, paymentRequirement));


        Long productPlanId = null;
        if (productType == ProductType.ADD_ON) {
            productPlanId = addOnPlanId;
        } else if (productType == ProductType.SUBSCRIPTION) {
            productPlanId = subscriptionPlanId;
        }

        // Apply coupon discount if valid and not tied to same discount plan
        // If coupon and discount plan are both present, use coupon
        if (coupon.isPresent() &&
                coupon.get().getDiscountPlan() != null) {
            amount = computeAmountWithCoupon(amount, coupon.get());
        }

        // Apply discount plan discount
        boolean isNotDuplicateDiscountPlan = discountPlan.isPresent()
                && coupon.isPresent()
                && coupon.get().getDiscountPlan() != null
                && !Objects.equals(coupon.get().getDiscountPlan().getId(), discountPlanId);

        if ((discountPlan.isPresent() && coupon.isEmpty()) ||
                (discountPlan.isPresent() && !isNotDuplicateDiscountPlan)) {
            amount = computeAmountWithDiscountPlan(amount, discountPlan.get());
        }

        // Prepare and save invoice
        Invoice invoice = prepareInvoice(
                totalAmount,
                amount,
                productType,
                productPlanId,
                userEntityType,
                userEntityId);

        invoice.setDiscountPlan(null);
        if (!isNotDuplicateDiscountPlan) {
            invoice.setDiscountPlan(discountPlan.orElse(null));
        }

        invoice.setCoupon(coupon.orElse(null));
        coupon.ifPresent(value -> invoice.setCouponCode(value.getCode()));
        return invoiceRepository.save(invoice);
    }

    @Override
    public void saveInvoice(Invoice invoice) {
        log.info("Saving invoice with id: {}", invoice.getId());
        invoiceRepository.save(invoice);
    }

    /**
     * Retrieves the base amount for the product.
     */
    private BigDecimal getBaseAmount(ProductType productType, Long addOnPlanId, Long subscriptionPlanId) {
        if (addOnPlanId != null && subscriptionPlanId != null) {
            throw new BadRequestException("Both add-on plan and subscription plan cannot be specified at the same time.");
        }
//        if (productType == ProductType.ADD_ON && addOnPlanId != null) {
//            return Optional.ofNullable(AddOnPlanMapper.toEntity(addOnPlanService.getAddOnPlan(addOnPlanId)))
//                    .map(AddOnPlan::getPriceAmount)
//                    .orElse(BigDecimal.ZERO);
//        }
//        if (productType == ProductType.SUBSCRIPTION && subscriptionPlanId != null) {
//            return Optional.ofNullable(SubscriptionPlanMapper.toEntity(subscriptionPlanService.getSubscriptionPlan(subscriptionPlanId)))
//                    .map(SubscriptionPlan::getPriceAmount)
//                    .orElse(BigDecimal.ZERO);
//        }

        CountryDto country = CountryContextHolder.getCountry();

        if (productType == ProductType.ADD_ON && addOnPlanId != null) {
            AddOnPlan addOnPlan = Optional.ofNullable(addOnPlanService.getAddOnPlan(addOnPlanId))
                    .map(AddOnPlanMapper::toEntity)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Add-on plan not found for ID: " + addOnPlanId + " in country: " + country.getName()));

            return Optional.ofNullable(addOnPlan.getPriceAmount())
                    .orElse(BigDecimal.ZERO);
        }

        if (productType == ProductType.SUBSCRIPTION && subscriptionPlanId != null) {
            SubscriptionPlan subscriptionPlan = Optional.ofNullable(
                            subscriptionPlanService.getSubscriptionPlan(subscriptionPlanId))
                    .map(SubscriptionPlanMapper::toEntity)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Subscription plan not found for ID: " + subscriptionPlanId + " in country: " + country.getName()));

            return Optional.ofNullable(subscriptionPlan.getPriceAmount())
                    .orElse(BigDecimal.ZERO);
        }

        return BigDecimal.ZERO;
    }


    private BigDecimal getCreditsIncluded(ProductType productType, Long productPlanId) {
        if (productPlanId == null) {
            throw new BadRequestException("Product plan ID cannot be null.");
        }
        if (productType == ProductType.ADD_ON) {
            return Optional.ofNullable(AddOnPlanMapper.toEntity(addOnPlanService.getAddOnPlan(productPlanId)))
                    .map(AddOnPlan::getCreditsIncluded)
                    .orElse(BigDecimal.ZERO);
        }
        if (productType == ProductType.SUBSCRIPTION) {
            return Optional.ofNullable(SubscriptionPlanMapper.toEntity(subscriptionPlanService.getSubscriptionPlan(productPlanId)))
                    .map(SubscriptionPlan::getCreditsIncluded)
                    .orElse(BigDecimal.ZERO);
        }
        return BigDecimal.ZERO;
    }

    @NotNull
    private Invoice prepareInvoice(BigDecimal totalAmount,
                                   BigDecimal amount,
                                   ProductType productType,
                                   Long productPlanId,
                                   UserEntityType userEntityType,
                                   Long userEntityId) {
        BigDecimal discountAmount = totalAmount.subtract(amount);
        Country country = CountryMapper.toEntity(CountryContextHolder.getCountry());

        Invoice invoice = new Invoice();
        invoice.setProductType(productType);

        invoice.setProductPlanId(productPlanId);

        invoice.setCountry(country);
        invoice.setCurrency(country.getCurrency());
        invoice.setDiscountAmount(discountAmount);
        invoice.setSubTotalAmount(amount);

        // Get tax amount based on the country
        BigDecimal taxAmount = taxRuleService.computeTaxAmount(amount, country);
        List<Long> taxRuleIds = taxRuleService.getTaxRuleIdsByCountry(country);

        invoice.setTaxAmount(taxAmount);
        invoice.setTotalAmount(amount.add(taxAmount));

        invoice.setCreditsIncluded(getCreditsIncluded(productType, productPlanId));

        invoice.setTaxRuleIds(taxRuleIds.toArray(new Long[0]));
        invoice.setIssuedAt(LocalDateTime.now());
        invoice.setStatus(InvoiceStatusEnum.SENT);
        invoice.setDueDate(LocalDateTime.now().plusDays(7));
        invoice.setRegion(null);

        // Generate invoice number
        String invoiceNumber = sequenceNumberGenerator.generateInvoiceNumber(country.getCountryIsoCode2());
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setPayerEntityType(userEntityType);
        invoice.setPayerEntityId(userEntityId);
        invoice.setNote("Invoice for " + productType.name() + " for user with ID: " + userEntityId);
        invoice.setExtraData(null);
        return invoice;
    }

    private BigDecimal computeAmountWithDiscountPlan(BigDecimal amount, DiscountPlan discountPlan) {
        return discountPlanService.computeAmountWithDiscountPlan(amount, discountPlan);
    }

    private BigDecimal computeAmountWithCoupon(BigDecimal amount, Coupon coupon) {
        return couponService.computeAmountWithCoupon(amount, coupon);
    }

}
