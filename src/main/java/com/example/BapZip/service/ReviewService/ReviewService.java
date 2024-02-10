package com.example.BapZip.service.ReviewService;

import com.example.BapZip.web.dto.ReviewRequestDTO;
import com.example.BapZip.web.dto.ReviewResponseDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReviewService {

    // 리뷰 작성
    @Transactional
    Long save(Long userId, ReviewRequestDTO.RegisterReviewDTO registerReviewDTO, List<String> urls);


    // 나의 리뷰 조회
    List<ReviewResponseDTO.MyReviewsDTO> getMyReview(Long userId);

    // 리뷰 삭제
    @Transactional
    void deleteReview(Long reviewId);

    // 리뷰 좋아요 하기
    void addLike( Long userId, Long reviewId);

    // 리뷰 좋아요 해제
    void deleteLike( Long userId, Long reviewId );

    // 좋아요한 리뷰 조회
    List<ReviewResponseDTO.ZipReviewDTO> findLikedReviews(Long userId);


    // 리뷰 랭킹 조회
    List<ReviewResponseDTO.ReviewRankingDTO> reviewRanking (Long userId);

    // 리뷰 타임라인 조회
    List<ReviewResponseDTO.TimelineDTO> reviewTimeline(Long userId, Long schoolId, String categoryName);

    // 가게 리뷰 조회
    List<ReviewResponseDTO.StoreReviewDTO> findStoreReview(Long userId, Long storeId, Long schoolId);



}
