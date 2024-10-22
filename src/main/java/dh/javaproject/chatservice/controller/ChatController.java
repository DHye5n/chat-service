package dh.javaproject.chatservice.controller;

import dh.javaproject.chatservice.dtos.ChatMessage;
import dh.javaproject.chatservice.dtos.ChatroomDto;
import dh.javaproject.chatservice.entities.Chatroom;
import dh.javaproject.chatservice.entities.Message;
import dh.javaproject.chatservice.services.ChatService;
import dh.javaproject.chatservice.vos.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatroomDto createChatroom(@AuthenticationPrincipal CustomOAuth2User user, @RequestParam(name = "title") String title) {

        Chatroom chatroom = chatService.createChatroom(user.getMember(), title);

        return ChatroomDto.from(chatroom);
    }

    @PostMapping("/{chatroomId}")
    public Boolean joinChatroom(
            @AuthenticationPrincipal CustomOAuth2User user,
            @PathVariable(name = "chatroomId") Long chatroomId,
            @RequestParam(name = "currentChatroomId", required = false) Long currentChatroomId) {

        return chatService.joinChatroom(user.getMember(), chatroomId, currentChatroomId);
    }

    @DeleteMapping("/{chatroomId}")
    public Boolean leaveChatroom(@AuthenticationPrincipal CustomOAuth2User user, @PathVariable(name = "chatroomId") Long chatroomId) {

        return chatService.leaveChatroom(user.getMember(), chatroomId);
    }

    @GetMapping
    public List<ChatroomDto> getChatroomList(@AuthenticationPrincipal CustomOAuth2User user) {

        List<Chatroom> chatroomList = chatService.getChatroomList(user.getMember());

        return chatroomList.stream()
                .map(ChatroomDto::from)
                .toList();
    }

    @GetMapping("/{chatroomId}/messages")
    public List<ChatMessage> getMessageList(@PathVariable(name = "chatroomId") Long chatroomId) {

        List<Message> messageList = chatService.getMessageList(chatroomId);

        messageList.forEach(message -> log.info("Message: {}, Member: {}", message.getText(), message.getMember()));

        return messageList.stream()
                .map(message -> new ChatMessage(message.getMember().getNickname(), message.getText()))
                .toList();
    }

}
