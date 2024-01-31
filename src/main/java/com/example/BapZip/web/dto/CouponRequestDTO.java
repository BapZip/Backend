package com.example.BapZip.web.dto;

import lombok.*;

public class CouponRequestDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class CouponIssueDTO{
        private Long issuedPoints;
        private Long totalPoints;
    }
}
