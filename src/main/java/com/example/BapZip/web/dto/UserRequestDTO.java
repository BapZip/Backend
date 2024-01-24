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

        private String major;

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


}
