package ch.rafaelurben.tamtour.voting.websockets.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AdminCommandEvent(
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) Long[] viewerIds,
    String action,
    Object data) {
  @JsonProperty("type")
  public String getType() {
    return "command";
  }
}
