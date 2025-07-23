package ch.rafaelurben.tamtour.voting.security;

import ch.rafaelurben.tamtour.voting.model.VotingUser;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

  @GetMapping("/whoami")
  public Map<String, Object> whoami(@AuthenticationPrincipal CustomUserPrincipal principal) {
    List<String> roles =
        principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    VotingUser user = principal.getUser();

    return Map.of(
        "user", user,
        "roles", roles,
        "isAdmin", roles.contains("ROLE_ADMIN"));
  }
}
