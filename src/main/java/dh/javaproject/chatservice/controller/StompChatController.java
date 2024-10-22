package dh.javaproject.chatservice.controller;

import dh.javaproject.chatservice.dtos.ChatMessage;
import dh.javaproject.chatservice.entities.Message;
import dh.javaproject.chatservice.services.ChatService;
import dh.javaproject.chatservice.vos.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class StompChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chats/{chatroomId}")
    @SendTo("/sub/chats/{chatroomId}")
    public ChatMessage handleMessage(Principal principal,
                                     @DestinationVariable("chatroomId") Long chatroomId,
                                     @Payload Map<String, String> payload) {

        log.info("{} sent {} in {}", principal.getName(), chatroomId, payload);

        CustomOAuth2User user = (CustomOAuth2User) ((AbstractAuthenticationToken) principal).getPrincipal();

        Message message = chatService.saveMessage(user.getMember(), chatroomId, payload.get("message"));

        messagingTemplate.convertAndSend("/sub/chats/updates", chatService.getChatroom(chatroomId));

        return new ChatMessage(principal.getName(), payload.get("message"));
    }
}
