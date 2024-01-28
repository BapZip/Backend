package com.example.BapZip.web.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

public class CongestionResponseDTO {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static public class registerCongestion{

        private Long Point; // 적립 포인트

    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static public class getCongestionRanking{
        Long storeId;
        String storeName;
        String storeImageURL;
        Integer waitingCount;
        Integer waitingTime;
        boolean bookmark;


    }
}
