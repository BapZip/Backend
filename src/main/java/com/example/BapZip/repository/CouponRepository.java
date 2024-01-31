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
    //findByUserIdAndFinalDateAfterAndFinalDateEqual
    List<Coupon> findByUserIdAndFinalDateAfterAndStatus(Long user_id, LocalDate finalDate, CouponStatus status);

    List<Coupon> findByUserIdAndFinalDateBefore(Long user_id, LocalDate finalDate);

    @Query("SELECT c FROM Coupon c WHERE (c.user.id = :userId AND c.finalDate < :finalDate) OR (c.user.id = :userId AND c.status = 'INVALID')")
    List<Coupon> findExpiredOrInvalidCouponsByUserId(
            @Param("userId") Long userId,
            @Param("finalDate") LocalDate finalDate
    );
}
