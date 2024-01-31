package com.example.BapZip.web.dto;

import com.example.BapZip.domain.School;
import lombok.*;

public class UserResonseDTO {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinDTO{
        Long id; // 사용자 id값
    }


    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class loginDTO{
        String token;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class checkNicknameAndIdDTO{
        boolean available;
    }





}
