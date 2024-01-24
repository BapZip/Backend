package com.example.BapZip.web.dto;

import lombok.*;

import java.util.List;

public class TestResponseDTO {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class testDTO{
        List<String> urls;
        Long userId;
        String nickname;
    }
}
