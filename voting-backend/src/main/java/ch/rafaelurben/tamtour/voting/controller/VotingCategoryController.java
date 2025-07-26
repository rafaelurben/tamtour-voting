package ch.rafaelurben.tamtour.voting.controller;

import ch.rafaelurben.tamtour.voting.dto.VotingCategoryUserDetailDto;
import ch.rafaelurben.tamtour.voting.dto.VotingCategoryUserOverviewDto;
import ch.rafaelurben.tamtour.voting.model.VotingUser;
import ch.rafaelurben.tamtour.voting.security.CustomUserPrincipal;
import ch.rafaelurben.tamtour.voting.service.VotingCategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class VotingCategoryController {
  private final VotingCategoryService votingCategoryService;

  @GetMapping("")
  public List<VotingCategoryUserOverviewDto> getCategoryOverview(
      @AuthenticationPrincipal CustomUserPrincipal principal) {
    VotingUser user = principal.getUser();

    return votingCategoryService.getCategoryOverviewForUser(user);
  }

  @GetMapping("{id}")
  public VotingCategoryUserDetailDto getCategory(
      @PathVariable Long id, @AuthenticationPrincipal CustomUserPrincipal principal) {
    VotingUser user = principal.getUser();

    return votingCategoryService.getCategoryForUser(id, user);
  }
}
