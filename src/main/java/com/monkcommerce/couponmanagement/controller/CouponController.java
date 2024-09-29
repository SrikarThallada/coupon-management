package com.monkcommerce.couponmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monkcommerce.couponmanagement.dto.CartDTO;
import com.monkcommerce.couponmanagement.dto.CouponDTO;
import com.monkcommerce.couponmanagement.service.CouponService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequestMapping("/coupon")
public class CouponController {
    private CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    public ResponseEntity<Object> createCoupon(@RequestBody CouponDTO couponDTO) throws Exception {
        log.info("Request to create coupon: {}", couponDTO);
        return new ResponseEntity<>(couponService.createCoupon(couponDTO),
                org.springframework.http.HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Object> getAllCoupons() {
        return new ResponseEntity<>(couponService.getAllCoupons(), org.springframework.http.HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCouponById(@PathVariable Long id) {
        return new ResponseEntity<>(couponService.getCouponById(id), org.springframework.http.HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCoupon(@PathVariable Long id, @RequestBody CouponDTO couponDTO)
            throws Exception {
        System.out.println("CouponController.getCouponById" + id);
        return new ResponseEntity<>(couponService.updateCoupon(id, couponDTO), org.springframework.http.HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return new ResponseEntity<>(org.springframework.http.HttpStatus.OK);
    }

    @PostMapping("/applicable-coupons")
    public ResponseEntity<Object> getApplicableCoupons(@RequestBody CartDTO cartDTO) {
        return new ResponseEntity<>(couponService.getApplicableCoupons(cartDTO),
                org.springframework.http.HttpStatus.OK);
    }

    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<Object> applyCoupon(@RequestBody CartDTO cartDTO, @PathVariable Long id) {
        return new ResponseEntity<>(couponService.applyCoupon(id, cartDTO),
                org.springframework.http.HttpStatus.OK);
    }

}
