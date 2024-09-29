package com.monkcommerce.couponmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monkcommerce.couponmanagement.entity.Coupon;
import com.monkcommerce.couponmanagement.enums.CouponType;

import java.util.List;

@Repository
public interface CouponRepo extends JpaRepository<Coupon, Long> {
    List<Coupon> findByThresholdLessThanAndType(Double threshold, CouponType type);
}
