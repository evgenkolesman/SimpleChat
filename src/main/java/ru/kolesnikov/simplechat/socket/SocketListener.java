package ru.kolesnikov.simplechat.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class SocketListener {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection");
    }

//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//
//        String login = Optional.ofNullable(headerAccessor
//                .getSessionAttributes()
//                .get("username"))
//                .orElseThrow(UserNotFoundException::new)
//                .toString();
//        log.info("User Disconnected : " + login);
//        simpMessageSendingOperations.convertAndSend("/public", new MessageDTO(login, ""));
//    }
}
