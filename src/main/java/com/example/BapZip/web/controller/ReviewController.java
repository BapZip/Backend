package com.example.BapZip.web.controller;

import com.example.BapZip.apiPayload.ApiResponse;
import com.example.BapZip.service.ReviewService.ReviewService;
import com.example.BapZip.service.S3Service.AmazonS3Service;
import com.example.BapZip.web.dto.ReviewRequestDTO;
import com.example.BapZip.web.dto.ReviewResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final AmazonS3Service s3Service;

    // 리뷰 작성
    @PostMapping( )
    @Operation(summary = "리뷰 작성 API", description = "form-data로 전송합니다. 리뷰를 작성합니다.")
    public ResponseEntity<ApiResponse<Long>> create(@AuthenticationPrincipal String  userId, @ModelAttribute ReviewRequestDTO.RegisterReviewDTO registerReviewDTO,  @RequestPart(value = "images1", required = false) List<MultipartFile> images1) {

        // 이미지 업로드
        List<String> urls = s3Service.uploadFiles("test", images1); // 이미지 업로드

        // 리뷰 작성 및 이미지 URL 추가
        // registerReviewDTO.setImages(urls); // 업로드된 이미지 URL로 설정
        Long id = reviewService.save(Long.valueOf(userId), registerReviewDTO, urls);

        String message = "10포인트 적립 완료";
        ApiResponse<Long> response = new ApiResponse<>(true, "COMMON200", message, id);

        return ResponseEntity.ok().body(response);
    }

    // 나의 리뷰 조회

    @GetMapping("/myReviews")
    @Operation(summary = "나의 리뷰 목록 조회 API", description = "내가 쓴 리뷰 목록들을 조회합니다.")
    public ApiResponse<List<ReviewResponseDTO.MyReviewsDTO>> getMyReviews(@AuthenticationPrincipal String  userId) {

        return ApiResponse.onSuccess(reviewService.getMyReview(Long.valueOf(userId)));

    }



    // 리뷰 삭제
    @DeleteMapping("/delete/{reviewId}")
    @Operation(summary = "리뷰 삭제 API", description = "내가 쓴 리뷰를 삭제합니다.")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    // 리뷰 좋아요 하기
    @PostMapping("/zip/{reviewId}")
    @Operation(summary = "리뷰 좋아요 하기 API", description = "리뷰를 좋아요합니다. ")
    public ResponseEntity<?> addLike(@AuthenticationPrincipal String userId, @PathVariable Long reviewId) {
        reviewService.addLike(Long.valueOf(userId), reviewId);

        ApiResponse<Void> response = new ApiResponse<>(true, "COMMON201", "리뷰를 zip했습니다.", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 리뷰 좋아요 해제
    @DeleteMapping("/deleteZip/{reviewId}")
    @Operation(summary = "리뷰 좋아요 해제 API", description = "리뷰 좋아요를 해제합니다.")
    public ResponseEntity<?> deleteLike(@AuthenticationPrincipal String userId,@PathVariable Long reviewId){
        reviewService.deleteLike(Long.valueOf(userId), reviewId);

        ApiResponse<Void> response = new ApiResponse<>(true, "COMMON204", "리뷰를 zip해제했습니다.",null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // 좋아요한 리뷰 조회
    @GetMapping("/myZipReviews")
    @Operation(summary = "좋아요 한 리뷰 목록 조회 API", description = "좋아요 한 리뷰 목록들을 조회합니다.")
    public ApiResponse<List<ReviewResponseDTO.ZipReviewDTO>> getMyLikedReviews(@AuthenticationPrincipal String userId) {
        // 좋아요한 리뷰 조회 로직 구현
        //List<ReviewResponseDTO.ZipReviewDTO> likedReveiws = reviewService.findLikedReviews(Long.valueOf(userId));

        return ApiResponse.onSuccess(reviewService.findLikedReviews(Long.valueOf(userId)));
    }


    // 리뷰 랭킹(좋아요 순)
    @GetMapping("/ranking")
    @Operation(summary = "리뷰 랭킹 조회 API", description = "좋아요 주간 리뷰 랭킹 TOP3를 조회합니다. ")
    public ApiResponse<List<ReviewResponseDTO.ReviewRankingDTO>> getReviewRanking(@AuthenticationPrincipal String userId) {

        return ApiResponse.onSuccess(reviewService.reviewRanking(Long.valueOf(userId)));


    }


    // 리뷰 타임라인 (카테고리별)
    @GetMapping("/timeline")
    @Operation(summary = "리뷰 타임라인 조회 API", description = "카테고리별 최신 리뷰 3개를 조회합니다. ")
    public ApiResponse<List<ReviewResponseDTO.TimelineDTO>> getReviewTimeline(@AuthenticationPrincipal String userId, Long schoolId, String categoryName) {

        return ApiResponse.onSuccess(reviewService.reviewTimeline(Long.valueOf(userId),schoolId, categoryName));
    }


    // 가게 리뷰 조회
    @GetMapping("/{storeId}")
    @Operation(summary = "가게 리뷰 조회 API", description = "가게 리뷰를 조회합니다. ")
    public ApiResponse<List<ReviewResponseDTO.StoreReviewDTO>> getStoreReview(@AuthenticationPrincipal String userId, @PathVariable("storeId") Long storeId, Long schoolId) {

        return ApiResponse.onSuccess(reviewService.findStoreReview(Long.valueOf(userId), storeId, schoolId));
    }




}
