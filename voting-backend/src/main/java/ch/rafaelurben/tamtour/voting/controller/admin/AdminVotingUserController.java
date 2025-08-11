package ch.rafaelurben.tamtour.voting.controller.admin;

import ch.rafaelurben.tamtour.voting.dto.VotingUserDto;
import ch.rafaelurben.tamtour.voting.security.UserRoles;
import ch.rafaelurben.tamtour.voting.service.admin.AdminVotingUserService;
import jakarta.annotation.security.RolesAllowed;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RolesAllowed(UserRoles.ADMIN)
@RequiredArgsConstructor
public class AdminVotingUserController {
  private final AdminVotingUserService adminVotingUserService;

  @GetMapping("")
  public List<VotingUserDto> getUsers() {
    return adminVotingUserService.getAllUsers();
  }

  @PostMapping("{userId}/block")
  public VotingUserDto blockUser(@PathVariable Long userId) {
    return adminVotingUserService.blockUser(userId);
  }
}
