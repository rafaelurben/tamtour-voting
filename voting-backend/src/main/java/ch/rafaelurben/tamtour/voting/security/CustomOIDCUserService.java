package ch.rafaelurben.tamtour.voting.security;

import ch.rafaelurben.tamtour.voting.model.VotingUser;
import ch.rafaelurben.tamtour.voting.repository.VotingUserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOIDCUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

  private final VotingUserRepository userRepository;

  @Value("${app.admin_emails}")
  private List<String> adminEmails;

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    OidcUserService delegate = new OidcUserService();
    OidcUser oidcUser = delegate.loadUser(userRequest);

    // Ensure the user email is verified
    Boolean emailVerified = oidcUser.getEmailVerified();
    if (emailVerified == null || !emailVerified) {
      throw new OAuth2AuthenticationException(
          new OAuth2Error("email_not_verified"),
          "Email not verified. Please verify your Google account.");
    }

    // Get/create user
    VotingUser user =
        userRepository
            .findBySub(oidcUser.getSubject())
            .orElseGet(
                () -> {
                  // Create user on first login
                  return userRepository.save(
                      VotingUser.builder()
                          .sub(oidcUser.getSubject())
                          .accountName(oidcUser.getFullName())
                          .accountEmail(oidcUser.getEmail())
                          .firstName(oidcUser.getGivenName())
                          .lastName(oidcUser.getFamilyName())
                          .pictureLink(oidcUser.getPicture())
                          .build());
                });

    // Give ROLE_USER to everyone
    Set<GrantedAuthority> authorities = new HashSet<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

    // Give ROLE_ADMIN based on email
    if (adminEmails.contains(user.getAccountEmail())) {
      authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    return new CustomUserPrincipal(user, authorities, oidcUser.getIdToken());
  }
}
