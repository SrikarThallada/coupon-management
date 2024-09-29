package com.monkcommerce.couponmanagement.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class CouponExceptionHandler {
    @ExceptionHandler(value = { CouponException.class })
    public ResponseEntity<Object> handleCouponNotFoundException(CouponException e) {
        return new ResponseEntity<>(e.getMessage(), org.springframework.http.HttpStatus.BAD_REQUEST);
    }
}
