package com.example.BapZip.service.CouponService;

import com.example.BapZip.apiPayload.code.status.ErrorStatus;
import com.example.BapZip.apiPayload.exception.GeneralException;
import com.example.BapZip.domain.Coupon;
import com.example.BapZip.domain.Point;
import com.example.BapZip.domain.User;
import com.example.BapZip.domain.enums.CouponStatus;
import com.example.BapZip.repository.CouponRepository;
import com.example.BapZip.repository.PointRepository;
import com.example.BapZip.repository.UserRepository;
import com.example.BapZip.web.dto.CouponRequestDTO;
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
    private final PointRepository pointRepository;
    private final UserRepository userRepository;

    @Override
    public List<CouponResponseDTO.CouponDTO> getValidCoupons(Long userId) {
        LocalDate currentDate = LocalDate.now();
        List<Coupon> validCoupons = couponRepository.findByUserIdAndFinalDateAfterAndStatus(userId, currentDate, CouponStatus.VALID);

        return validCoupons.stream()
                .map(this::mapToCouponDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponResponseDTO.CouponDTO> getInvalidCoupons(Long userId) {
        LocalDate currentDate = LocalDate.now();
        //List<Coupon> invalidCoupons = couponRepository.findByUserIdAndFinalDateBefore(userId, currentDate);
        List<Coupon> invalidCoupons = couponRepository.findExpiredOrInvalidCouponsByUserId(userId, currentDate);
        return invalidCoupons.stream()
                .map(this::mapToCouponDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CouponResponseDTO.CouponDTO issuedCoupon(Long userId, CouponRequestDTO.CouponIssueDTO dto) {
        User user = userRepository.findById(Long.valueOf(userId)).get();


        if (dto.getIssuedPoints() > dto.getTotalPoints()) { // 잔액 부족 시,
            throw new GeneralException(ErrorStatus.INSUFFICIENT_BALANCE_ERROR);
        }

        Coupon coupon = Coupon.builder()
                .user(user)
                .startDate(LocalDate.now())
                .finalDate(LocalDate.now().plusYears(1))
                .amount(dto.getIssuedPoints())
                .build();

        Coupon coupon_result = couponRepository.save(coupon);

        Point point = Point.builder()
                .user(user)
                .note("쿠폰 발행")
                .point(dto.getIssuedPoints() * -1)
                .classification("사용")
                .build();

        Point point_result = pointRepository.save(point);

        return mapToCouponDTO(coupon);

    }

    private CouponResponseDTO.CouponDTO mapToCouponDTO(Coupon coupon) {
        // 객체에서 뽑아서 빌더로 reponse DTO 생성
        return CouponResponseDTO.CouponDTO.builder()
                .couponId(coupon.getId())
                .amount(coupon.getAmount())
                .startDate(coupon.getStartDate())
                .finalDate(coupon.getFinalDate()).build();
    }
}
