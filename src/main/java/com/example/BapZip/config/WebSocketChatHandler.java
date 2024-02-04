package com.example.BapZip.config;

import com.example.BapZip.domain.enums.ChatMessageType;
import com.example.BapZip.repository.UserRepository;
import com.example.BapZip.service.ChatService.ChatService;
import com.example.BapZip.web.dto.ChatDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/*
 * WebSocket Handler 작성
 * 소켓 통신은 서버와 클라이언트가 1:n으로 관계를 맺는다. 따라서 한 서버에 여러 클라이언트 접속 가능
 * 서버에는 여러 클라이언트가 발송한 메세지를 받아 처리해줄 핸들러가 필요
 * TextWebSocketHandler를 상속받아 핸들러 작성
 * 클라이언트로 받은 메세지를 log로 출력하고 클라이언트로 환영 메세지를 보내줌
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper;
    private final ChatService chatService;
    private final UserRepository userRepository;

    // 현재 연결된 세션들
    private final Set<WebSocketSession> sessions = new HashSet<>();

    // chatRoomId: {session1, session2}
    private final Map<Long,Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    // 소켓 연결 확인
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // TODO Auto-generated method stub
        log.info("{} 연결됨", session.getId());
        sessions.add(session);
    }

    // 소켓 통신 시 메세지의 전송을 다루는 부분
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {} session {}", payload,session.getId());

        // 페이로드 -> chatMessageDto로 변환
        ChatDTO.ChatMessageRequestDTO chatMessageDto = mapper.readValue(payload, ChatDTO.ChatMessageRequestDTO.class);

        // chatMessageType이 null인 경우 기본값 설정 또는 오류 처리
        if (chatMessageDto.getChatMessageType() == null) {
            log.error("chatMessageType is null for message: {}", payload);
            // 기본값 설정 또는 오류 처리
            return;
        }

        Long storeId = chatMessageDto.getStoreId();
        // 메모리 상에 채팅방에 대한 세션 없으면 만들어줌
        if(!chatRoomSessionMap.containsKey(storeId)){
            chatRoomSessionMap.put(storeId,new HashSet<>());
        }
        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(storeId);

        // message 에 담긴 타입을 확인한다.
        // 이때 message 에서 getType 으로 가져온 내용이
        // ChatDTO 의 열거형인 MessageType 안에 있는 ENTER 과 동일한 값이라면
        if (chatMessageDto.getChatMessageType().equals(ChatMessageType.ENTER)) {
            // sessions 에 넘어온 session 을 담고,
            chatRoomSession.add(session);

        }else{
            //일단 이건 메세지 저장메소드
            chatService.saveMessage(chatMessageDto);

            if (chatRoomSession.size()>=3) {
                removeClosedSession(chatRoomSession);
            }
            ChatDTO.ChatMessageResponseDTO chatMessageResponseDTO= ChatDTO.ChatMessageResponseDTO
                            .builder().message(chatMessageDto.getMessage()).timestamp(LocalDateTime.now())
                            .nickname(userRepository.findById(chatMessageDto.getUserId()).get().getNickname())
                                    .userId(chatMessageDto.getUserId()).storeId(chatMessageDto.getStoreId())
                            .build();

            sendMessageToChatRoom(chatMessageResponseDTO, chatRoomSession);
        }
    }

    // 소켓 종료 확인
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // TODO Auto-generated method stub
        log.info("{} 연결 끊김", session.getId());
        sessions.remove(session);
    }

    // ====== 채팅 관련 메소드 ======
    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(sess -> !sessions.contains(sess));
    }

    private void sendMessageToChatRoom(ChatDTO.ChatMessageResponseDTO chatMessageDto, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream().forEach(sess -> sendMessage(sess, chatMessageDto));//2
    }


    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}