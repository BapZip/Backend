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

        private String storeName;
        private Integer rating;
        private List<String> hashtags;
        private String reviewText;
        private LocalDate visitDate;

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
