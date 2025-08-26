package ch.rafaelurben.tamtour.voting.websockets;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

  private final ViewerWebSocketHandler viewerHandler;
  private final AdminWebSocketHandler adminHandler;
  private final ViewerTokenHandshakeInterceptor viewerTokenHandshakeInterceptor;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry
        .addHandler(viewerHandler, "/api/viewer/ws")
        .setAllowedOrigins("*")
        .addInterceptors(viewerTokenHandshakeInterceptor);
    registry.addHandler(adminHandler, "/api/admin/viewer/ws");
  }
}
