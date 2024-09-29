package com.monkcommerce.couponmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monkcommerce.couponmanagement.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

}