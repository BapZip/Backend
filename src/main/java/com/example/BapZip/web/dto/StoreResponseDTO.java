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
    public static class StoreThumbnailDTO {

        List<String> images;
        String name;

        Boolean bookmark;
        InOrOut inOrOut;

        // 도메인에 없지만 내가 새로 만든 속성
        Integer waitingExpected;

        // 도메인에 없지만 내가 새로 만든 속성
        Float ratingStars;

        List<String> mostHashtag;




    }

    public static class StoreInfoDTO {

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
