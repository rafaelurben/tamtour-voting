package ch.rafaelurben.tamtour.voting.websockets.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ConnectedViewersEvent(Long[] keyIds) {
  @JsonProperty("type")
  public String getType() {
    return "connected_viewers";
  }
}
