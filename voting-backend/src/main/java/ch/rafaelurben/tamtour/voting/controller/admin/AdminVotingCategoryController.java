package ch.rafaelurben.tamtour.voting.controller.admin;

import ch.rafaelurben.tamtour.voting.dto.VotingCandidateDto;
import ch.rafaelurben.tamtour.voting.dto.VotingCategoryBaseDto;
import ch.rafaelurben.tamtour.voting.dto.admin.*;
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

  @PostMapping("")
  public VotingCategoryBaseDto createCategory(@RequestBody VotingCategoryRequestDto category) {
    return adminVotingCategoryService.createCategory(category);
  }

  @GetMapping("/{id}")
  public VotingCategoryBaseDto getCategory(@PathVariable Long id) {
    return adminVotingCategoryService.getCategory(id);
  }

  @PutMapping("/{id}")
  public VotingCategoryBaseDto updateCategory(
      @PathVariable Long id, @RequestBody VotingCategoryRequestDto updateDto) {
    return adminVotingCategoryService.updateCategory(id, updateDto);
  }

  @DeleteMapping("/{id}")
  public void deleteCategory(@PathVariable Long id) {
    adminVotingCategoryService.deleteCategory(id);
  }

  @GetMapping("/{id}/result")
  public VotingCategoryResultDto getCategoryResult(@PathVariable Long id) {
    return adminVotingCategoryService.getCategoryResult(id);
  }

  @GetMapping("/{id}/sets")
  public Set<VotingSetDto> getVotingSets(@PathVariable Long id) {
    return adminVotingCategoryService.getVotingSets(id);
  }

  @PatchMapping("/{id}/sets/{setId}")
  public VotingSetDto updateVotingSet(
      @PathVariable Long id, @PathVariable Long setId, @RequestBody VotingSetUpdateDto updateDto) {
    return adminVotingCategoryService.updateVotingSet(id, setId, updateDto);
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

  @PutMapping("/{id}/candidates/{candidateId}")
  public VotingCandidateDto updateCandidate(
      @PathVariable Long id,
      @PathVariable Long candidateId,
      @RequestBody VotingCandidateRequestDto candidate) {
    return adminVotingCategoryService.updateCandidate(id, candidateId, candidate);
  }

  @DeleteMapping("/{id}/candidates/{candidateId}")
  public void deleteCandidate(@PathVariable Long id, @PathVariable Long candidateId) {
    adminVotingCategoryService.deleteCandidate(id, candidateId);
  }
}
