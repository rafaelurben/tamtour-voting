package ch.rafaelurben.tamtour.voting.websockets.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ViewerEvent(String action, Long keyId) {
  @JsonProperty("type")
  public String getType() {
    return "viewer_event";
  }
}
