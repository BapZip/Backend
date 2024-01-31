package com.example.BapZip.web.dto;

import com.example.BapZip.domain.School;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class UserRequestDTO {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinDTO{
        private String userId;

        private String password;

        private String nickname;

        private Long school;

        private Long major;

        private String email;

    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDTO {
        private String userId;

        private String password;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermDTO {
        private Long userId;
        private String term1;
        private String term2;
        private String term3;
        private String term4;

    }


}
