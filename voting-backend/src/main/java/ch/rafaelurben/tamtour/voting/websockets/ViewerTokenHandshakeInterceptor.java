package ch.rafaelurben.tamtour.voting.websockets;

import ch.rafaelurben.tamtour.voting.model.ResultViewerKey;
import ch.rafaelurben.tamtour.voting.repository.ResultViewerKeyRepository;
import jakarta.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
@RequiredArgsConstructor
public class ViewerTokenHandshakeInterceptor implements HandshakeInterceptor {
  public static final String ATTR_KEY_ID = "resultViewerKeyId";

  private final ResultViewerKeyRepository resultViewerKeyRepository;

  @Override
  public boolean beforeHandshake(
      @Nonnull ServerHttpRequest request,
      @Nonnull ServerHttpResponse response,
      @Nonnull WebSocketHandler wsHandler,
      @Nonnull Map<String, Object> attributes) {

    String token = null;

    if (request instanceof ServletServerHttpRequest servletRequest) {
      token = servletRequest.getServletRequest().getParameter("token");
    }

    if (token == null || !token.matches("^[0-9a-fA-F\\-]{36}$")) {
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      return false;
    }

    UUID tokenUuid = UUID.fromString(token);
    Optional<ResultViewerKey> keyOpt = resultViewerKeyRepository.findByKey(tokenUuid);
    if (keyOpt.isEmpty()) {
      response.setStatusCode(HttpStatus.FORBIDDEN);
      return false;
    }

    attributes.put(ATTR_KEY_ID, keyOpt.get().getId());
    return true;
  }

  @Override
  public void afterHandshake(
      @Nonnull ServerHttpRequest req,
      @Nonnull ServerHttpResponse res,
      @Nonnull WebSocketHandler handler,
      Exception ex) {
    /* No handling needed */
  }
}
