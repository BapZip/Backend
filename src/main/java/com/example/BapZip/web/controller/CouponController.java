package com.example.BapZip.web.controller;


import com.example.BapZip.repository.CouponRepository;
import com.example.BapZip.service.CouponService.CouponService;
import com.example.BapZip.web.dto.CouponResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "사용가능 쿠폰 API", description = "사용가능 쿠폰 조회 API입니다.")
    @GetMapping("/available")
    public CouponResponseDTO.CouponDTO ValidCoupon(@AuthenticationPrincipal String userId){

        return null;
    }

    @Operation(summary = "만료된 쿠폰 API", description = "만료된 쿠폰 조회 API입니다.")
    @GetMapping("/expiration")
    public CouponResponseDTO.CouponDTO InValidCoupon(@AuthenticationPrincipal String userId){

        return null;
    }
}
