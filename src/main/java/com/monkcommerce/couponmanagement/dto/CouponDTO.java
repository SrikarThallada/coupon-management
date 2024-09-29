package com.monkcommerce.couponmanagement.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CouponDTO {
    private Long id;
    @NotNull(message = "Coupon Type is required")
    private String type;
    private LocalDate expirationDate;
    private JsonNode details;
}
