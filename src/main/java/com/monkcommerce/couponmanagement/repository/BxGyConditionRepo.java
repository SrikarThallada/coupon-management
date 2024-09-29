package com.monkcommerce.couponmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monkcommerce.couponmanagement.entity.BxGyCondition;

@Repository
public interface BxGyConditionRepo extends JpaRepository<BxGyCondition, Long> {

    Optional<BxGyCondition> findByCouponId(Long couponId);

}
