package com.monkcommerce.couponmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicableDiscountDTO {
    private Long couponId;
    private String type;
    private double discount;
}