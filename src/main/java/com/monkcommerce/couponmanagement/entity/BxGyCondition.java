package com.monkcommerce.couponmanagement.entity;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "bxgy_conditions")
@Data
public class BxGyCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode buyProducts; // List of product IDs to be bought

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode getProducts; // List of product IDs to be given for free

    private Integer repetitionLimit; // For BxGy repetition limits

}
