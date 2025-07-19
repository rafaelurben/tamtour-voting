package ch.rafaelurben.tamtour.voting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            authz -> authz.requestMatchers("/actuator/**").permitAll().anyRequest().authenticated())
        .oauth2Login(
            oauth2 ->
                oauth2
                    .loginPage("/api/oauth2/authorization/google")
                    .defaultSuccessUrl("/app", true)
                    .authorizationEndpoint(authz -> authz.baseUri("/api/oauth2/authorization"))
                    .redirectionEndpoint(redir -> redir.baseUri("/api/login/oauth2/code/*")))
        .logout(
            logout ->
                logout.logoutUrl("/api/logout").logoutSuccessUrl("/").deleteCookies("JSESSIONID"))
        .csrf(csrf -> csrf.ignoringRequestMatchers("/api/logout"));
    return http.build();
  }
}
