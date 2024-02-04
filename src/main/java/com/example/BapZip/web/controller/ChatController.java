package com.example.BapZip.web.controller;

import com.example.BapZip.apiPayload.ApiResponse;
import com.example.BapZip.config.WebSocketChatHandler;
import com.example.BapZip.service.ChatService.ChatService;
import com.example.BapZip.web.dto.ChatDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

   // private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketChatHandler webSocketChatHandler;
    private final ChatService chatService;

    @GetMapping("/{storeId}/messages")
    public ApiResponse<List<ChatDTO.ChatMessageResponseDTO>> getMessages(@PathVariable Long storeId) {
        List<ChatDTO.ChatMessageResponseDTO> messages = chatService.getMessageByStoreId(storeId);
        return ApiResponse.onSuccess(messages); // 특정 가게의 채팅 메시지 조회
    }
}
