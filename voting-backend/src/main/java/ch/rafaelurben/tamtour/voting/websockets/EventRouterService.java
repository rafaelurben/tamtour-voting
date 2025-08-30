package ch.rafaelurben.tamtour.voting.websockets;

import ch.rafaelurben.tamtour.voting.websockets.events.AdminCommandEvent;
import ch.rafaelurben.tamtour.voting.websockets.events.AdminConnectedEvent;
import ch.rafaelurben.tamtour.voting.websockets.events.ConnectedViewersEvent;
import ch.rafaelurben.tamtour.voting.websockets.events.ViewerEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventRouterService {
  private final ViewerWebSocketHandler viewerHandler;
  private final AdminWebSocketHandler adminHandler;
  private final ObjectMapper objectMapper;

  private void sendToAdmins(String message) {
    adminHandler.broadcastMessageToAllAdmins(message);
  }

  private void sendToViewers(Long[] viewerIds, String message) {
    viewerHandler.sendMessageToViewers(viewerIds, message);
  }

  private String toJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
      return null;
    }
  }

  @EventListener
  public void handleViewerEvent(ViewerEvent event) {
    sendToAdmins(toJson(event));
  }

  @EventListener
  public void handleAdminCommandEvent(AdminCommandEvent event) {
    sendToViewers(event.viewerIds(), toJson(event));
  }

  @EventListener
  public void handleAdminConnectedEvent(AdminConnectedEvent event) {
    ConnectedViewersEvent connectedViewersEvent =
        new ConnectedViewersEvent(viewerHandler.getCurrentViewerIds());
    adminHandler.sendMessage(event.session(), toJson(connectedViewersEvent));
  }
}
