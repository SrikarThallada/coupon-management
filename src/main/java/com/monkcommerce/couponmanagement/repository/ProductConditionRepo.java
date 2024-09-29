package com.monkcommerce.couponmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monkcommerce.couponmanagement.entity.ProductCondition;
import java.util.List;

@Repository
public interface ProductConditionRepo extends JpaRepository<ProductCondition, Long> {

    Optional<ProductCondition> findByCouponId(Long couponId);

    List<ProductCondition> findByProductId(Long productId);

    Optional<ProductCondition> findByProductIdAndCouponId(Long productId, Long couponId);
}
