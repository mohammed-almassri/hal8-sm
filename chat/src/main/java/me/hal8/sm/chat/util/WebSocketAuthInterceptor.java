package me.hal8.sm.chat.util;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {
    private  JWTUtil jwtUtil;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (Objects.equals(accessor.getDestination(), "/app/authenticate")) {
            String jwtToken = new String((byte[]) message.getPayload());
            if (jwtUtil.validateToken(jwtToken)) {
                var user =  jwtUtil.getSubject(jwtToken);
                accessor.setUser(()->user);
                var a = jwtUtil.getClaimFromToken(jwtToken,(Claims::getSubject));
                System.out.println("Authenticated user: " + a);
            } else {
                throw new IllegalArgumentException("Invalid JWT Token");
            }
        }

        return message;
    }
}
