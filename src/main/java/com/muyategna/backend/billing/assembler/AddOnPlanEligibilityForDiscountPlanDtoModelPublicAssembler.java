package com.muyategna.backend.billing.assembler;

import com.muyategna.backend.billing.dto.add_on_plan_discount_eligibility.AddOnPlanDiscountEligibilityDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class AddOnPlanEligibilityForDiscountPlanDtoModelPublicAssembler {
    public EntityModel<AddOnPlanDiscountEligibilityDto> toModel(AddOnPlanDiscountEligibilityDto addOnPlanDiscountEligibilityDto,
                                                                HttpServletRequest request) {
        return EntityModel.of(addOnPlanDiscountEligibilityDto);
    }


    public CollectionModel<EntityModel<AddOnPlanDiscountEligibilityDto>> toCollectionModel(Iterable<? extends AddOnPlanDiscountEligibilityDto> addOnPlanEligibilityForDiscountPlanDtos,
                                                                                           HttpServletRequest request) {
        List<EntityModel<AddOnPlanDiscountEligibilityDto>> addOnPlanEligibilityForDiscountPlanDtoList = StreamSupport.stream(addOnPlanEligibilityForDiscountPlanDtos.spliterator(), false)
                .map(addOnPlanEligibilityForDiscountPlanDto -> this.toModel(addOnPlanEligibilityForDiscountPlanDto, request))
                .toList();
        return CollectionModel.of(addOnPlanEligibilityForDiscountPlanDtoList);
    }
}
