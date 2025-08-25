package ch.rafaelurben.tamtour.voting.websockets;

import static ch.rafaelurben.tamtour.voting.websockets.ViewerTokenHandshakeInterceptor.ATTR_KEY_ID;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
public class ViewerWebSocketHandler extends TextWebSocketHandler {
  private static final String MSG_HEARTBEAT = "{\"type\":\"heartbeat\"}";
  private static final String MSG_CONNECTED = "{\"type\":\"connected\"}";
  private static final String MSG_DUPLICATE_KEY =
      "{\"type\":\"error\", \"message\":\"Another viewer has connected with the same key. This session will be closed.\"}";

  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  private final Map<Long, WebSocketSession> keyIdToSessionMap = new ConcurrentHashMap<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    Long keyId = (Long) session.getAttributes().get(ATTR_KEY_ID);
    log.info("Connection established for key id: {}", keyId);
    if (keyIdToSessionMap.containsKey(keyId)) {
      WebSocketSession existingSession = keyIdToSessionMap.get(keyId);
      sendMessage(existingSession, MSG_DUPLICATE_KEY);
      try {
        existingSession.close(new CloseStatus(1000, "Duplicate connection"));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    keyIdToSessionMap.put(keyId, session);
    sendMessage(session, MSG_CONNECTED);
  }

  @Override
  public void afterConnectionClosed(
      @Nonnull WebSocketSession session, @Nonnull CloseStatus status) {
    Long keyId = (Long) session.getAttributes().get(ATTR_KEY_ID);
    keyIdToSessionMap.remove(keyId);
    log.info("Connection closed for key id: {} with status: {}", keyId, status);
  }

  @Override
  protected void handleTextMessage(
      @Nonnull WebSocketSession session, @Nonnull TextMessage message) {
    Long keyId = (Long) session.getAttributes().get(ATTR_KEY_ID);
    log.debug("Received message from viewer with key id {}: {}", keyId, message.getPayload());
  }

  @PostConstruct
  public void startHeartbeat() {
    log.debug("Starting viewer heartbeat");
    scheduler.scheduleAtFixedRate(
        () -> {
          for (WebSocketSession session : keyIdToSessionMap.values()) {
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
              Long keyId = (Long) session.getAttributes().get(ATTR_KEY_ID);
              keyIdToSessionMap.remove(keyId);
            }
          }
        },
        10,
        25,
        TimeUnit.SECONDS);
  }

  private void sendMessage(WebSocketSession session, String msg) {
    try {
      session.sendMessage(new TextMessage(msg));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendMessageToViewer(Long keyId, String msg) {
    WebSocketSession session = keyIdToSessionMap.get(keyId);
    if (session != null && session.isOpen()) {
      sendMessage(session, msg);
    } else {
      log.error("No open session found for key id: {}", keyId);
    }
  }
}
