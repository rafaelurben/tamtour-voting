package ch.rafaelurben.tamtour.voting.websockets;

import ch.rafaelurben.tamtour.voting.websockets.events.AdminCommandEvent;
import ch.rafaelurben.tamtour.voting.websockets.events.AdminConnectedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
public class AdminWebSocketHandler extends TextWebSocketHandler {
  private static final String MSG_HEARTBEAT = "{\"type\":\"heartbeat\"}";
  private static final String MSG_CONNECTED = "{\"type\":\"connected\"}";

  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
  private final ObjectMapper objectMapper;
  private final ApplicationEventPublisher applicationEventPublisher;

  public AdminWebSocketHandler(
      ObjectMapper objectMapper, ApplicationEventPublisher applicationEventPublisher) {
    this.objectMapper = objectMapper;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public void afterConnectionEstablished(@Nonnull WebSocketSession session) {
    log.info("Connection established for admin.");
    sessions.add(session);
    sendMessage(session, MSG_CONNECTED);
    applicationEventPublisher.publishEvent(new AdminConnectedEvent(session));
  }

  @Override
  public void afterConnectionClosed(
      @Nonnull WebSocketSession session, @Nonnull CloseStatus status) {
    sessions.remove(session);
    log.info("Connection closed for admin with status: {}", status);
  }

  @Override
  protected void handleTextMessage(
      @Nonnull WebSocketSession session, @Nonnull TextMessage message) {
    log.debug("Received message from admin: {}", message.getPayload());
    try {
      AdminCommandEvent event =
          objectMapper.readValue(message.getPayload(), AdminCommandEvent.class);
      applicationEventPublisher.publishEvent(event);
    } catch (JsonProcessingException e) {
      log.error("Failed to parse admin message: {}", e.getMessage());
    }
  }

  @PostConstruct
  public void startHeartbeat() {
    log.debug("Starting admin heartbeat");
    scheduler.scheduleAtFixedRate(
        () -> {
          for (WebSocketSession session : sessions) {
            try {
              if (session.isOpen()) {
                session.sendMessage(new TextMessage(MSG_HEARTBEAT));
                return;
              }
            } catch (IOException _) {
              try {
                session.close();
              } catch (IOException _) {
                // Ignore
              }
              sessions.remove(session);
            }
          }
        },
        10,
        25,
        TimeUnit.SECONDS);
  }

  public void sendMessage(WebSocketSession session, String msg) {
    try {
      session.sendMessage(new TextMessage(msg));
    } catch (IOException e) {
      log.error("Failed to send message to admin WebSocket session", e);
    }
  }

  public void broadcastMessageToAllAdmins(String msg) {
    for (WebSocketSession session : sessions) {
      sendMessage(session, msg);
    }
  }
}
