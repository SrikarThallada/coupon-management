package com.monkcommerce.couponmanagement.entity;

import jakarta.persistence.CascadeType;
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
@Table(name = "carts")
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "total_discount")
    private Double totalDiscount = 0.0;

    @Column(name = "final_price")
    private Double finalPrice;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    private Coupon coupon;

    // Getters and Setters
}
