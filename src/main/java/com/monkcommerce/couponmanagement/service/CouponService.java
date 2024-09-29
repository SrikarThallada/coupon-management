package com.monkcommerce.couponmanagement.service;

import java.util.List;

import com.monkcommerce.couponmanagement.dto.ApplicableCouponsDTO;
import com.monkcommerce.couponmanagement.dto.CartDTO;
import com.monkcommerce.couponmanagement.dto.CouponDTO;
import com.monkcommerce.couponmanagement.dto.UpdateCartDTO;

public interface CouponService {

    CouponDTO createCoupon(CouponDTO coupon) throws Exception;

    List<CouponDTO> getAllCoupons();

    CouponDTO getCouponById(Long id);

    CouponDTO updateCoupon(Long id, CouponDTO couponDTO) throws Exception;

    void deleteCoupon(Long id);

    ApplicableCouponsDTO getApplicableCoupons(CartDTO cart);

    UpdateCartDTO applyCoupon(Long couponId, CartDTO cart);
}
