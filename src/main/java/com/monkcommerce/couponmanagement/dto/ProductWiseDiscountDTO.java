package com.monkcommerce.couponmanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductWiseDiscountDTO {
    @JsonProperty("product_id")
    private Long productId;
    private double discount;
}
