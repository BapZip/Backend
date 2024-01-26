package com.example.BapZip.repository;

import com.example.BapZip.domain.Coupon;
import com.example.BapZip.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByUserIdAndFinalDateAfter(Long user_id, LocalDate finalDate);

    List<Coupon> findByUserIdAndFinalDateBefore(Long user_id, LocalDate finalDate);
}
