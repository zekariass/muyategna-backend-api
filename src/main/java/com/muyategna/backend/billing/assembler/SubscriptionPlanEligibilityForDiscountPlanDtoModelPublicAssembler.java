package com.muyategna.backend.billing.assembler;

import com.muyategna.backend.billing.dto.subscription_plan_discount_eligibility.SubscriptionPlanDiscountEligibilityDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class SubscriptionPlanEligibilityForDiscountPlanDtoModelPublicAssembler {
    public EntityModel<SubscriptionPlanDiscountEligibilityDto> toModel(SubscriptionPlanDiscountEligibilityDto subscriptionPlanDiscountEligibilityDto, HttpServletRequest request) {
        return EntityModel.of(subscriptionPlanDiscountEligibilityDto);
    }


    public CollectionModel<EntityModel<SubscriptionPlanDiscountEligibilityDto>> toCollectionModel(Iterable<? extends SubscriptionPlanDiscountEligibilityDto> subscriptionPlanEligibilityForDiscountPlanDtos, HttpServletRequest request) {
        List<EntityModel<SubscriptionPlanDiscountEligibilityDto>> subscriptionPlanEligibilityForDiscountPlanDtoModels = StreamSupport
                .stream(subscriptionPlanEligibilityForDiscountPlanDtos.spliterator(), false)
                .map(subscriptionPlanEligibilityForDiscountPlanDto -> toModel(subscriptionPlanEligibilityForDiscountPlanDto, request))
                .toList();
        return CollectionModel.of(subscriptionPlanEligibilityForDiscountPlanDtoModels);
    }
}
