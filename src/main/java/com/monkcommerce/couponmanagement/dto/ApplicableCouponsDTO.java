package com.monkcommerce.couponmanagement.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicableCouponsDTO {
    private List<ApplicableDiscountDTO> applicableCoupons;
}
