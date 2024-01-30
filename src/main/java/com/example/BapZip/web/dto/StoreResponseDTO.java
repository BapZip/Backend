package com.example.BapZip.web.dto;


import com.example.BapZip.domain.Store;
import com.example.BapZip.domain.enums.InOrOut;
import lombok.*;

import java.util.List;

//  가게 썸네일, 가게 정보, 오늘의 공지 DTO 만들기
public class StoreResponseDTO {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoreInfoDTO {

        List<String> images;
        String name;
        Boolean bookmark;
        String inOrOut;
        Integer waitTime;
        Double score;
        List<String> hashtag;

    }

    public static class StoreDetailInfoDTO {

        String waitingAverage;

        String getBusinessHours;

        String closeDay;

        String position;

    }


    public static class StoreNoticeDTO {

        String content;

        Store store;




    }
}
