package ch.rafaelurben.tamtour.voting.controller.admin;

import ch.rafaelurben.tamtour.voting.dto.VotingCandidateDto;
import ch.rafaelurben.tamtour.voting.dto.VotingCategoryBaseDto;
import ch.rafaelurben.tamtour.voting.dto.admin.VotingCandidateRequestDto;
import ch.rafaelurben.tamtour.voting.security.UserRoles;
import ch.rafaelurben.tamtour.voting.service.admin.AdminVotingCategoryService;
import jakarta.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories")
@RolesAllowed(UserRoles.ADMIN)
@RequiredArgsConstructor
public class AdminVotingCategoryController {
  private final AdminVotingCategoryService adminVotingCategoryService;

  @GetMapping("")
  public List<VotingCategoryBaseDto> getCategories() {
    return adminVotingCategoryService.getAllCategories();
  }

  @GetMapping("/{id}/candidates")
  public Set<VotingCandidateDto> getCandidates(@PathVariable Long id) {
    return adminVotingCategoryService.getCandidates(id);
  }

  @PostMapping("/{id}/candidates")
  public VotingCandidateDto addCandidate(
      @PathVariable Long id, @RequestBody VotingCandidateRequestDto candidate) {
    return adminVotingCategoryService.addCandidate(id, candidate);
  }
}
