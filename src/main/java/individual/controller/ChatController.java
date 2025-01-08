package individual.controller;

import individual.domain.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/events/{id}/chat")
    @SendTo("/topic/events/{id}/chat")
    public ChatMessage sendMessage(@DestinationVariable String id, ChatMessage chatMessage) {
        return chatMessage;
    }
}

