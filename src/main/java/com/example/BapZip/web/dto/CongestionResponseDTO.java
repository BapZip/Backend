package com.example.BapZip.web.dto;

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
        Integer occupancyCount;
        Integer waitingTime;
        Integer congestionAV; // 혼잡도 평균 81점 만점
        boolean bookmark;
        int ranking;

    }
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static public class getCongestionRankingTop5{
        Long storeId;
        String storeName;
        Integer waitingTime;
        Integer congestionAV; // 혼잡도 평균 81점 만점
        int ranking;
    }
}
