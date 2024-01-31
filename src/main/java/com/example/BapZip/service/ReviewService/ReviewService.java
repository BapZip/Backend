package com.example.BapZip.service.ReviewService;

import com.example.BapZip.domain.Review;
import com.example.BapZip.repository.ReviewRepository;
import com.example.BapZip.web.dto.ReviewRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReviewService {

    // 리뷰 작성
    @Transactional
    Long save(Long userId, ReviewRequestDTO.RegisterReviewDTO registerReviewDTO);

    // 나의 리뷰 조회
    @Transactional
    List<Review> findByUserId(Long userId);

    // 리뷰 삭제
    @Transactional
    void deleteReview(Long reviewId);

    // 리뷰 좋아요 하기
    void addLike(Long reviewId, Long userId);

    // 리뷰 좋아요 해제
    void deleteLike(Long reviewId, Long userId);
}
