package com.example.BapZip.web.dto;

import com.example.BapZip.domain.enums.ChatMessageType;
import lombok.*;

import java.time.LocalDateTime;

public class ChatDTO {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static public class ChatMessageRequestDTO {
        private Long storeId;
        private String userId;
        private String message;
        private ChatMessageType chatMessageType;
        // getter, setter, constructor 등
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static public class ChatMessageResponseDTO {
        private Long messageId;
        private Long storeId;
        private String userId;
        private String nickname;
        private String message;
        private LocalDateTime timestamp;
        // getter, setter, constructor 등
    }


}
