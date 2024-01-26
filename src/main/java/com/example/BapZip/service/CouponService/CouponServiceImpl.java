package com.example.BapZip.service.CouponService;

import com.example.BapZip.domain.Coupon;
import com.example.BapZip.repository.CouponRepository;
import com.example.BapZip.web.dto.CouponResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{
    private final CouponRepository couponRepository;

    @Override
    public List<CouponResponseDTO.CouponDTO> getInvalidCoupons(Long userId) {
        LocalDate currentDate = LocalDate.now();
        List<Coupon> validCoupons = couponRepository.findByUserIdAndFinalDateAfter(userId, currentDate);

        return validCoupons.stream()
                .map(this::mapToCouponDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponResponseDTO.CouponDTO> getValidCoupons(Long userId) {
        LocalDate currentDate = LocalDate.now();
        List<Coupon> invalidCoupons = couponRepository.findByUserIdAndFinalDateBefore(userId, currentDate);

        return invalidCoupons.stream()
                .map(this::mapToCouponDTO)
                .collect(Collectors.toList());
    }

    private CouponResponseDTO.CouponDTO mapToCouponDTO(Coupon coupon) {
        return CouponResponseDTO.CouponDTO.builder()
                .amount(coupon.getAmount())
                .startDate(coupon.getStartDate())
                .finalDate(coupon.getFinalDate())
                .build();
    }
}
