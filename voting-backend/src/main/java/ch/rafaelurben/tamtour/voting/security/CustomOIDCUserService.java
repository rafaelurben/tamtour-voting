package ch.rafaelurben.tamtour.voting.security;

import ch.rafaelurben.tamtour.voting.model.VotingUser;
import ch.rafaelurben.tamtour.voting.repository.VotingUserRepository;
import java.time.OffsetDateTime;
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
          "E-Mail nicht bestätigt. Bitte bestätige die E-Mail-Adresse in deinem Google-Account!");
    }

    // Get/create user
    VotingUser user =
        userRepository
            .findBySub(oidcUser.getSubject())
            .orElseGet(
                () ->
                    userRepository.save(
                        VotingUser.builder()
                            .sub(oidcUser.getSubject())
                            .accountName(oidcUser.getFullName())
                            .accountEmail(oidcUser.getEmail())
                            .firstName(oidcUser.getGivenName())
                            .lastName(oidcUser.getFamilyName())
                            .pictureLink(oidcUser.getPicture())
                            .build()));

    // Prevent sign-in if user is blocked
    if (user.isBlocked()) {
      throw new OAuth2AuthenticationException(
          new OAuth2Error("blocked_user"),
          "Dein Account ist gesperrt. Bitte kontaktiere den Administrator.");
    }

    // Update last login
    user.setLastLoginAt(OffsetDateTime.now());
    user = userRepository.save(user);

    // Give user role to everyone
    Set<GrantedAuthority> authorities = new HashSet<>();
    authorities.add(new SimpleGrantedAuthority(UserRoles.ROLE_USER));

    // Give admin role based on email
    if (adminEmails.contains(user.getAccountEmail())) {
      authorities.add(new SimpleGrantedAuthority(UserRoles.ROLE_ADMIN));
    }

    return new CustomUserPrincipal(user, authorities, oidcUser.getIdToken());
  }
}
