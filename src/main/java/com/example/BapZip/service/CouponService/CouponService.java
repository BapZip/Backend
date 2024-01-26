package com.example.BapZip.service.CouponService;

import com.example.BapZip.web.dto.CouponResponseDTO;

import java.util.List;

public interface CouponService {
    List<CouponResponseDTO.CouponDTO> getValidCoupons(Long userId);

    List<CouponResponseDTO.CouponDTO> getInvalidCoupons(Long userId);
}
