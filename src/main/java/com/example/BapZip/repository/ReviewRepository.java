package com.example.BapZip.repository;

import com.example.BapZip.domain.Review;
import com.example.BapZip.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.BapZip.domain.mapping.UserStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByStore(Store store);


    // 특정 사용자의 모든 리뷰를 검색하기 위한 쿼리 메서드 (리스트로 리뷰 목록 쉽게 O) (최신순 정렬)
    List<Review> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    List<Review> findByImagesImageUrl(String imageUrl);

    List<Review> findByStore_IdAndPaymentTimeBetween(Long storeId,LocalDate startDate,LocalDate endDate);


    List<Review> findTop3ByStoreOrderByCreatedAtDesc(Store store);

    // 가게 리뷰 조회 (최신 리뷰 순으로 정렬)
    List<Review> findAllByStoreOrderByCreatedAtDesc(Store store);

    // 리뷰 랭킹 조회 (리뷰 작성 시점으로 일주일)
    @Query("SELECT r.user.id, COUNT(ur) " +
            "FROM Review r JOIN UserReview ur ON r.id = ur.review.id " +
            "WHERE  r.createdAt >= :oneWeekAgo " +
            "GROUP BY r.user.id " +
            "ORDER BY COUNT(ur) DESC")
    List<Object[]> countLikesByUserId(LocalDateTime oneWeekAgo);
}
