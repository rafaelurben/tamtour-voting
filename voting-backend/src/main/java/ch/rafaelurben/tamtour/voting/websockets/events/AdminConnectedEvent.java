package ch.rafaelurben.tamtour.voting.websockets.events;

import org.springframework.web.socket.WebSocketSession;

public record AdminConnectedEvent(WebSocketSession session) {}
