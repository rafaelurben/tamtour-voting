package ch.rafaelurben.tamtour.voting.security;

import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

  @GetMapping("/whoami")
  public Map<String, Object> whoami(@AuthenticationPrincipal OAuth2User user, Authentication auth) {
    List<String> roles =
        auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

    return Map.of(
        "name", user.getAttribute("name"),
        "email", user.getAttribute("email"),
        "picture", user.getAttribute("picture"),
        "id", user.getAttribute("sub"),
        "roles", roles,
        "isAdmin", roles.contains("ROLE_ADMIN"));
  }
}
