package com.example.BapZip.service.ChatService;

import com.example.BapZip.web.dto.ChatDTO;

import java.util.List;

public interface ChatService {

    void saveMessage(ChatDTO.ChatMessageRequestDTO request);

    List<ChatDTO.ChatMessageResponseDTO> getMessageByStoreId(Long storeId);
}
