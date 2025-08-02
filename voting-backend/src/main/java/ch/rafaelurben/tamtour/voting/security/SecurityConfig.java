package ch.rafaelurben.tamtour.voting.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomOIDCUserService customOIDCUserService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            authz -> authz.requestMatchers("/actuator/**").permitAll().anyRequest().authenticated())
        .oauth2Login(
            oauth2 ->
                oauth2
                    .loginPage("/api/oauth2/authorization/google")
                    .userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOIDCUserService))
                    .defaultSuccessUrl("/", true)
                    .failureHandler(
                        (_, res, ex) -> {
                          String url =
                              ServletUriComponentsBuilder.fromCurrentContextPath()
                                  .path("error")
                                  .queryParam("errorMessage", "Login fehlgeschlagen")
                                  .queryParam("errorDescription", ex.getMessage())
                                  .queryParam("backUrl", "/login")
                                  .toUriString();
                          res.setStatus(302);
                          res.setHeader("Location", url);
                        })
                    .authorizationEndpoint(authz -> authz.baseUri("/api/oauth2/authorization"))
                    .redirectionEndpoint(redir -> redir.baseUri("/api/login/oauth2/code/*")))
        .logout(
            logout ->
                logout
                    .logoutUrl("/api/logout")
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                    .logoutSuccessHandler((_, response, _) -> response.setStatus(200)))
        .exceptionHandling(
            e ->
                e.authenticationEntryPoint(
                    (_, response, _) -> response.sendError(401, "Unauthorized")))
        .csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }
}
