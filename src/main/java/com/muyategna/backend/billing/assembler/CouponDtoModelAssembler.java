package com.muyategna.backend.billing.assembler;

import com.muyategna.backend.billing.dto.coupon.CouponDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class CouponDtoModelAssembler {

    public EntityModel<CouponDto> toModel(CouponDto couponDto, HttpServletRequest request) {
        return EntityModel.of(couponDto);
    }
    
    public CollectionModel<EntityModel<CouponDto>> toCollectionModel(Iterable<? extends CouponDto> couponDtoList, HttpServletRequest request) {
        List<EntityModel<CouponDto>> couponDtoModels = StreamSupport
                .stream(couponDtoList.spliterator(), false)
                .map(couponDto -> toModel(couponDto, request))
                .toList();
        return CollectionModel.of(couponDtoModels);
    }
}
