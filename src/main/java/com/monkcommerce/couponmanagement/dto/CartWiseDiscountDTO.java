package com.monkcommerce.couponmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartWiseDiscountDTO {
    private double threshold;
    private double discount;
}
