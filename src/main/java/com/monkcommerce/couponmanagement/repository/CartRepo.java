package com.monkcommerce.couponmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monkcommerce.couponmanagement.entity.Cart;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {

}
