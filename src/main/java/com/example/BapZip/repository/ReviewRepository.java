package com.example.BapZip.repository;

import com.example.BapZip.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 특정 사용자의 모든 리뷰를 검색하기 위한 쿼리 메서드 (리스트로 리뷰 목록 쉽게 O)
    List<Review> findAllByUserId(Long userId);
}
