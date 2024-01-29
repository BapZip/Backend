package com.example.BapZip.web.dto;

import lombok.*;

import java.time.LocalDate;

public class PointResponseDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class PointHistoryDTO{
        private Long pointId;
        private Long point;
        private String classification;
        private String note;
    }
}
