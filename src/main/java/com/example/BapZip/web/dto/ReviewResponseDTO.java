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
        Long storeId;
        String storeName;
        String nickname;
        Integer rating;
        String reviewText;
        String imageUrl;
        LocalDate paymentTime;
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
        String imageUrl;
        LocalDate paymentTime;
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
        String reviewImage; // 리뷰 이미지들
        LocalDate paymentTime;


    }








}
