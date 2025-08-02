package ch.rafaelurben.tamtour.voting.controller;

import ch.rafaelurben.tamtour.voting.dto.UpdateMeDto;
import ch.rafaelurben.tamtour.voting.dto.VotingUserDto;
import ch.rafaelurben.tamtour.voting.dto.WhoamiDto;
import ch.rafaelurben.tamtour.voting.mapper.VotingUserMapper;
import ch.rafaelurben.tamtour.voting.model.VotingUser;
import ch.rafaelurben.tamtour.voting.security.CustomUserPrincipal;
import ch.rafaelurben.tamtour.voting.security.UserRoles;
import ch.rafaelurben.tamtour.voting.service.AuthService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;
  private final VotingUserMapper votingUserMapper;

  @GetMapping("/whoami")
  public WhoamiDto whoami(@AuthenticationPrincipal CustomUserPrincipal principal) {
    List<String> roles =
        principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    VotingUser user = principal.getUser();
    return new WhoamiDto(votingUserMapper.toDto(user), roles, roles.contains(UserRoles.ROLE_ADMIN));
  }

  @PatchMapping("/me")
  public VotingUserDto me(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @RequestBody @Valid UpdateMeDto updateMeDto) {
    VotingUser user = principal.getUser();
    user = authService.updateUser(user, updateMeDto);
    return votingUserMapper.toDto(user);
  }
}
