package ch.rafaelurben.tamtour.voting.security;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Component
public class CustomOIDCUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

  @Value("${app.admin_emails}")
  private List<String> adminEmails;

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    OidcUserService delegate = new OidcUserService();
    OidcUser user = delegate.loadUser(userRequest);

    // Ensure the user email is verified
    Boolean emailVerified = user.getAttribute("email_verified");
    if (emailVerified == null || !emailVerified) {
      throw new OAuth2AuthenticationException(
          new OAuth2Error("email_not_verified"),
          "Email not verified. Please verify your Google account.");
    }

    Set<GrantedAuthority> authorities = new HashSet<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

    // Give ROLE_ADMIN based on email
    String email = user.getAttribute("email");
    if (adminEmails.contains(email)) {
      authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    return new DefaultOidcUser(authorities, user.getIdToken(), "email");
  }
}
