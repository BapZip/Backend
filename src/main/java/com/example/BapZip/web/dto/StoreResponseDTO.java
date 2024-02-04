package com.example.BapZip.web.dto;


import com.example.BapZip.domain.Store;
import com.example.BapZip.domain.enums.InOrOut;
import com.example.BapZip.domain.enums.StoreListStaus;
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
        String category;
        List<String> hashtag;

    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoreDetailInfoDTO {

        String waitingAverage;

        String BusinessHours;

        String closedDay;

        String position;

    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoreNoticeDTO {

        String content;

        Store store;


    }
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrintedMenuDTO {

        Long id;

        String imageUrl;

    }
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyZipDTO {

        Long id;

        String name;

        String imageUrl;

    }
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HotPlaceDTO {

        Long Ranking;

        Long storeId;

        String name;

        String category;

        InOrOut inOut;

        Double score;

        String imageUrl;

    }


    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoreListReviewCountDTO{

        Long storeId;

        Long ReviewCount;

        Long Ranking;

        String name;

        String category;

        StoreListStaus storeListStaus;

        InOrOut inOut;

        String imageUrl;

        Boolean isMyZip;

    }
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoreListScoreDTO{

        Long storeId;

        Long Ranking;

        Double score;

        String name;

        String category;

        InOrOut inOut;

        String imageUrl;

        Boolean isMyZip;

        StoreListStaus storeListStaus;

    }
      //getNotice

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeDTO {

        String notice;
    }
    public static class StoreListRecommendDTO{

        Long storeId;

        Long ReviewCount;

        Long Ranking;

        Double score;

        Double totalScore;

        String name;

        String category;

        InOrOut inOut;

        String imageUrl;

        Boolean isMyZip;

        StoreListStaus storeListStaus;

    }
}

