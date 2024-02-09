package com.example.BapZip.service.ChatService;

import com.example.BapZip.domain.User;
import com.example.BapZip.domain.chat.ChatMessage;
import com.example.BapZip.domain.chat.ChatRoom;
import com.example.BapZip.repository.ChatMessageRepository;
import com.example.BapZip.repository.ChatRoomRepository;
import com.example.BapZip.repository.UserRepository;
import com.example.BapZip.web.dto.ChatDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {


    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public void saveMessage(ChatDTO.ChatMessageRequestDTO request) {
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        ChatRoom chatRoom = chatRoomRepository.findByStore_Id(request.getStoreId()).get();

        ChatMessage chatMessage = new ChatMessage(null, chatRoom, user, user.getNickname(),request.getMessage(), LocalDateTime.now());
        chatMessageRepository.save(chatMessage);
    }

    @Override
    public List<ChatDTO.ChatMessageResponseDTO> getMessageByStoreId(Long storeId) {
        return chatRoomRepository.findByStore_Id(storeId)
                .map(ChatRoom::getChatMessagesList)
                .orElseThrow(() -> new EntityNotFoundException("ChatRoom not found for Store ID: " + storeId))
                .stream()
                .map(msg -> ChatDTO.ChatMessageResponseDTO.builder()
                        .messageId(msg.getId())
                        .storeId(storeId)
                        .userId(msg.getUser().getUserId())
                        .nickname(msg.getUser().getNickname())
                        .message(msg.getMessage())
                        .timestamp(msg.getTimestamp())
                        .build())
                .collect(Collectors.toList());
    }

    // 추가적인 메소드 구현
}
