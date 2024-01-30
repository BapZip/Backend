package com.example.BapZip.repository;

import com.example.BapZip.domain.Review;
import com.example.BapZip.domain.mapping.UserStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByStore_IdAndPaymentTimeBetween(Long storeId,LocalDate startDate,LocalDate endDate);
}
