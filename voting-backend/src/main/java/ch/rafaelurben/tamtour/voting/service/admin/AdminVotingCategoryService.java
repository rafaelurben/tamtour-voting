package ch.rafaelurben.tamtour.voting.service.admin;

import ch.rafaelurben.tamtour.voting.dto.*;
import ch.rafaelurben.tamtour.voting.dto.admin.*;
import ch.rafaelurben.tamtour.voting.exceptions.ObjectNotFoundException;
import ch.rafaelurben.tamtour.voting.mapper.VotingCandidateMapper;
import ch.rafaelurben.tamtour.voting.mapper.VotingCategoryMapper;
import ch.rafaelurben.tamtour.voting.mapper.VotingSetMapper;
import ch.rafaelurben.tamtour.voting.model.VotingCandidate;
import ch.rafaelurben.tamtour.voting.model.VotingCategory;
import ch.rafaelurben.tamtour.voting.model.VotingPosition;
import ch.rafaelurben.tamtour.voting.model.VotingSet;
import ch.rafaelurben.tamtour.voting.repository.VotingCandidateRepository;
import ch.rafaelurben.tamtour.voting.repository.VotingCategoryRepository;
import ch.rafaelurben.tamtour.voting.repository.VotingSetRepository;
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
  private final VotingSetMapper votingSetMapper;
  private final VotingSetRepository votingSetRepository;
  private final ResultCalculatorService resultCalculatorService;

  public List<VotingCategoryBaseDto> getAllCategories() {
    return votingCategoryMapper.toBaseDto(votingCategoryRepository.findAll());
  }

  public VotingCategoryBaseDto createCategory(VotingCategoryRequestDto category) {
    VotingCategory votingCategory = votingCategoryMapper.toEntity(category);
    votingCategory = votingCategoryRepository.save(votingCategory);
    return votingCategoryMapper.toBaseDto(votingCategory);
  }

  private VotingCategory getCategoryById(Long id) {
    return votingCategoryRepository
        .findById(id)
        .orElseThrow(() -> new ObjectNotFoundException("Category not found with id: " + id));
  }

  public VotingCategoryBaseDto getCategory(Long id) {
    return votingCategoryMapper.toBaseDto(getCategoryById(id));
  }

  public VotingCategoryBaseDto updateCategory(Long categoryId, VotingCategoryRequestDto updateDto) {
    VotingCategory existingCategory = getCategoryById(categoryId);
    votingCategoryMapper.updateEntityFromDto(existingCategory, updateDto);
    existingCategory = votingCategoryRepository.save(existingCategory);
    return votingCategoryMapper.toBaseDto(existingCategory);
  }

  public VotingCategoryResultDto getCategoryResult(Long categoryId) {
    VotingCategory category = getCategoryById(categoryId);
    return resultCalculatorService.calculateResult(category);
  }

  public Set<VotingSetDto> getVotingSets(Long categoryId) {
    VotingCategory category = getCategoryById(categoryId);
    return votingSetMapper.toDto(category.getVotingSets());
  }

  public VotingSetDto updateVotingSet(Long categoryId, Long setId, VotingSetUpdateDto updateDto) {
    VotingSet existingSet =
        votingSetRepository
            .findByVotingCategoryIdAndId(categoryId, setId)
            .orElseThrow(
                () ->
                    new ObjectNotFoundException(
                        "Set not found with id: " + setId + " in category with id: " + categoryId));
    votingSetMapper.updateEntityFromDto(existingSet, updateDto);
    existingSet = votingSetRepository.save(existingSet);
    return votingSetMapper.toDto(existingSet);
  }

  public Set<VotingCandidateDto> getCandidates(Long categoryId) {
    VotingCategory category = getCategoryById(categoryId);
    return votingCandidateMapper.toDto(category.getVotingCandidates());
  }

  public VotingCandidateDto addCandidate(Long categoryId, VotingCandidateRequestDto candidate) {
    VotingCategory category = getCategoryById(categoryId);
    VotingCandidate votingCandidate = votingCandidateMapper.toEntity(candidate);
    votingCandidate.setVotingCategory(category);
    votingCandidate = votingCandidateRepository.save(votingCandidate);

    // Add candidate to all existing voting sets in this category
    for (VotingSet votingSet : category.getVotingSets()) {
      VotingPosition votingPosition =
          VotingPosition.builder()
              .position(null)
              .votingCandidate(votingCandidate)
              .votingSet(votingSet)
              .build();
      votingSet.getVotingPositions().add(votingPosition);
      votingSetRepository.save(votingSet);
    }

    return votingCandidateMapper.toDto(votingCandidate);
  }

  public VotingCandidateDto updateCandidate(
      Long categoryId, Long candidateId, VotingCandidateRequestDto updateDto) {
    VotingCandidate existingCandidate =
        votingCandidateRepository
            .findByVotingCategoryIdAndId(categoryId, candidateId)
            .orElseThrow(
                () ->
                    new ObjectNotFoundException(
                        "Candidate not found with id: "
                            + candidateId
                            + " in category with id: "
                            + categoryId));
    votingCandidateMapper.updateEntityFromDto(existingCandidate, updateDto);
    existingCandidate = votingCandidateRepository.save(existingCandidate);
    return votingCandidateMapper.toDto(existingCandidate);
  }
}
