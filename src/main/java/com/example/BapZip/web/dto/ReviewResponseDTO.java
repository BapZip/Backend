package com.example.BapZip.web.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewResponseDTO {
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyReviewsDTO {
        LocalDate visitDate;
        Long storeId;
        String storeName;
        Integer rating;
        String nickname;
        String UserImage;
        String reviewText;
        List<String> imageUrls; // 단일 이미지 -> 여러 이미지 수정
        LocalDateTime createdAt;
        Long reviewId;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ZipReviewDTO {
        Long storeId;
        String storeName;
        String nickname;
        Integer rating;
        String reviewText;
        String userImage;
        List<String> imageUrls; // 단일 이미지 -> 여러 이미지 수정
        LocalDateTime createdAt;
        Long reviewId;
    }


    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewRankingDTO {
        private String nickname;
        private String imageUrl;
        private Long likesCount; // 좋아요 수
    }




    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TimelineDTO{
        Long storeId;
        String storeName;
        String imageUrl; // Store 이미지
        String reviewText;
        String nickname;
        LocalDateTime ReviewCreateDate;
        Long categoryId;
        Boolean like;
        Long likesCount; // 좋아요 수
        Long reviewId;

    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StoreReviewDTO{
        Long storeId;
        String storeName;
        String nickname;
        Integer rating;
        String reviewText;
        String userImage; // User 프로필 사진
        List<String> reviewImages; // 리뷰이미지) 단일 이미지 -> 여러 이미지 수정
        LocalDateTime createdAt;
        Boolean like;
        Long reviewId;



    }








}
