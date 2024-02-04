package com.example.BapZip.web.controller;

import com.example.BapZip.apiPayload.ApiResponse;
import com.example.BapZip.domain.Point;
import com.example.BapZip.domain.Review;
import com.example.BapZip.service.ReviewService.ReviewService;
import com.example.BapZip.service.ReviewService.ReviewServiceImpl;
import com.example.BapZip.web.dto.ReviewRequestDTO;
import com.example.BapZip.web.dto.ReviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping("/")
    public ResponseEntity<ApiResponse<Long>> create(@AuthenticationPrincipal String  userId, @RequestBody ReviewRequestDTO.RegisterReviewDTO registerReviewDTO) {
        Long id = reviewService.save(Long.valueOf(userId), registerReviewDTO);

        String message = "10포인트 적립 완료";
        ApiResponse<Long> response = new ApiResponse<>(true, "COMMON200", message, id);

        return ResponseEntity.ok().body(response);
    }

    // 나의 리뷰 조회
    @GetMapping("/myReviews")
    public ResponseEntity<List<Review>> getMyReviews(@AuthenticationPrincipal String  userId) {
        List<Review> reviews = reviewService.findByUserId(Long.valueOf(userId));
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 삭제
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    // 리뷰 좋아요 하기
    @PostMapping("/zip/{reviewId}")
    public ResponseEntity<?> addLike(@PathVariable Long reviewId, @RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        reviewService.addLike(reviewId, userId);

        ApiResponse<Void> response = new ApiResponse<>(true, "COMMON201", "리뷰를 zip했습니다.", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 리뷰 좋아요 해제
    @DeleteMapping("/deleteZip/{reviewId}")
    public ResponseEntity<?> deleteLike(@PathVariable Long reviewId, @RequestBody Map<String, Long> body){
        Long userId = body.get("userId");
        reviewService.deleteLike(reviewId, userId);

        ApiResponse<Void> response = new ApiResponse<>(true, "COMMON204", "리뷰를 zip해제했습니다.",null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // 좋아요한 리뷰 조회
    @GetMapping("/myZipReviews")
    public ResponseEntity<List<Review>> getMyLikedReviews(@AuthenticationPrincipal String  userId) {
        List<Review> reviews = reviewService.findLikedReviews(Long.valueOf(userId));

        return ResponseEntity.ok(null);
    }


}
