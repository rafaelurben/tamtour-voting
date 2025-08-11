package ch.rafaelurben.tamtour.voting.service.admin;

import ch.rafaelurben.tamtour.voting.dto.*;
import ch.rafaelurben.tamtour.voting.dto.admin.VotingCandidateRequestDto;
import ch.rafaelurben.tamtour.voting.exceptions.ObjectNotFoundException;
import ch.rafaelurben.tamtour.voting.mapper.VotingCandidateMapper;
import ch.rafaelurben.tamtour.voting.mapper.VotingCategoryMapper;
import ch.rafaelurben.tamtour.voting.model.VotingCandidate;
import ch.rafaelurben.tamtour.voting.model.VotingCategory;
import ch.rafaelurben.tamtour.voting.repository.VotingCandidateRepository;
import ch.rafaelurben.tamtour.voting.repository.VotingCategoryRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminVotingCategoryService {
  private final VotingCategoryMapper votingCategoryMapper;
  private final VotingCategoryRepository votingCategoryRepository;
  private final VotingCandidateMapper votingCandidateMapper;
  private final VotingCandidateRepository votingCandidateRepository;

  public List<VotingCategoryBaseDto> getAllCategories() {
    return votingCategoryMapper.toBaseDto(votingCategoryRepository.findAll());
  }

  private VotingCategory getCategory(Long id) {
    return votingCategoryRepository
        .findById(id)
        .orElseThrow(() -> new ObjectNotFoundException("Category not found with id: " + id));
  }

  public Object getCategoryResult(Long categoryId) {
    VotingCategory category = getCategory(categoryId);
    return category.calculatePoints();
  }

  public Set<VotingCandidateDto> getCandidates(Long categoryId) {
    VotingCategory category = getCategory(categoryId);
    return votingCandidateMapper.toDto(category.getVotingCandidates());
  }

  public VotingCandidateDto addCandidate(Long categoryId, VotingCandidateRequestDto candidate) {
    VotingCategory category = getCategory(categoryId);
    VotingCandidate votingCandidate = votingCandidateMapper.toEntity(candidate);
    votingCandidate.setVotingCategory(category);
    votingCandidate = votingCandidateRepository.save(votingCandidate);
    return votingCandidateMapper.toDto(votingCandidate);
  }
}
