package com.example.BapZip.web.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewResponse {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewResponseDTO{
        private Integer point;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TimelineDTO{
        private Integer storeId;
        private String storeName;
        private String reviewText;
        private String nickname;
        private Date ReviewCreateDate;
        private Integer categoryId;
        private Boolean like;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TimelineListDTO{
        private List<TimelineDTO> timelineList = new ArrayList<>();
    }


}
