package ch.rafaelurben.tamtour.voting.security;

import ch.rafaelurben.tamtour.voting.model.VotingUser;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

@Getter
public class CustomUserPrincipal extends DefaultOidcUser {
  private final VotingUser user;

  public CustomUserPrincipal(
      VotingUser user, Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken) {
    super(authorities, idToken, "email");
    this.user = user;
  }
}
