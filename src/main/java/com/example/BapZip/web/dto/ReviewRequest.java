package com.example.BapZip.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

public class ReviewRequest {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterReviewDTO{
        private Date visitDate;
        private String storeName;
        private Integer price;
        private String menuName;
        private Integer rating;
        private List<Integer> hashtags;
        private String reviewText;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimelineDTO{
        private Integer schoolId;
        private Integer categoryId;
    }
}
