package com.example.BapZip.web.dto;

import lombok.*;

import java.time.LocalDate;

public class CouponResponseDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class CouponDTO{
        private Long amount;
        private LocalDate startDate;
        private LocalDate finalDate;
    }

}
