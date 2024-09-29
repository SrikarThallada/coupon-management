package com.monkcommerce.couponmanagement.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BxgyDiscountDTO {
    @JsonProperty("buy_products")
    private List<JsonNode> buyProducts;
    @JsonProperty("get_products")
    private List<JsonNode> getProducts;
    @JsonProperty("repetition_limit")
    private int repetitionLimit;
}
