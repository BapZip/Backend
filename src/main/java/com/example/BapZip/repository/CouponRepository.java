package com.example.BapZip.repository;

import com.example.BapZip.domain.Coupon;
import com.example.BapZip.domain.enums.CouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByUserIdAndFinalDateAfterAndStatus(Long user_id, LocalDate finalDate, CouponStatus status);

    // And보다 Or 조건이 우선적으로 적용되어야하기에 쿼리 사용.
    @Query("SELECT c FROM Coupon c WHERE (c.user.id = :userId AND c.finalDate < :finalDate) OR (c.user.id = :userId AND c.status = 'INVALID')")
    List<Coupon> findExpiredOrInvalidCouponsByUserId(
            @Param("userId") Long userId,
            @Param("finalDate") LocalDate finalDate
    );
}
