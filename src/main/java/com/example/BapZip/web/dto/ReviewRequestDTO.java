package com.example.BapZip.web.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ReviewRequestDTO {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterReviewDTO{
        private LocalDate visitDate;
        private String storeName;
        private Integer price;
        private String menuName;
        private Integer rating;
        private List<String> hashtags;
        private String reviewText;

        // 이미지 리스트 필드 추가
        // private List<String> images;
    }


    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimelineDTO{
        private Integer schoolId;
        private Integer categoryId;
    }
}
