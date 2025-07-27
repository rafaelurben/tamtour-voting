package ch.rafaelurben.tamtour.voting.service;

import ch.rafaelurben.tamtour.voting.dto.VotingCategoryUserDetailDto;
import ch.rafaelurben.tamtour.voting.dto.VotingCategoryUserOverviewDto;
import ch.rafaelurben.tamtour.voting.dto.VotingCategoryUserStateDto;
import ch.rafaelurben.tamtour.voting.dto.VotingPositionMapDto;
import ch.rafaelurben.tamtour.voting.exceptions.ObjectNotFoundException;
import ch.rafaelurben.tamtour.voting.mapper.VotingCandidateMapper;
import ch.rafaelurben.tamtour.voting.mapper.VotingCategoryMapper;
import ch.rafaelurben.tamtour.voting.model.VotingCategory;
import ch.rafaelurben.tamtour.voting.model.VotingPosition;
import ch.rafaelurben.tamtour.voting.model.VotingSet;
import ch.rafaelurben.tamtour.voting.model.VotingUser;
import ch.rafaelurben.tamtour.voting.repository.VotingCategoryRepository;
import ch.rafaelurben.tamtour.voting.repository.VotingSetRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VotingCategoryService {
  private final VotingCategoryMapper votingCategoryMapper;
  private final VotingCategoryRepository votingCategoryRepository;
  private final VotingSetRepository votingSetRepository;
  private final VotingCandidateMapper votingCandidateMapper;

  public List<VotingCategoryUserOverviewDto> getCategoryOverviewForUser(VotingUser user) {
    List<VotingCategory> categories = votingCategoryRepository.findAll();

    return categories.stream()
        .map(
            category -> {
              Optional<VotingSet> votingSet =
                  votingSetRepository.findByVotingUserAndVotingCategory(user, category);
              boolean exists = votingSet.isPresent();
              boolean submitted = exists && votingSet.get().isSubmitted();

              return new VotingCategoryUserOverviewDto(
                  votingCategoryMapper.toBaseDto(category),
                  new VotingCategoryUserStateDto(exists, submitted));
            })
        .toList();
  }

  private VotingSet createVotingSet(VotingUser user, VotingCategory category) {
    VotingSet votingSet =
        VotingSet.builder()
            .votingUser(user)
            .votingCategory(category)
            .votingPositions(
                category.getVotingCandidates().stream()
                    .map(
                        candidate ->
                            VotingPosition.builder()
                                .votingCandidate(candidate)
                                .position(null)
                                .build())
                    .collect(Collectors.toSet()))
            .build();
    return votingSetRepository.save(votingSet);
  }

  public VotingCategoryUserDetailDto getCategoryForUser(Long categoryId, VotingUser user) {
    VotingCategory category =
        votingCategoryRepository
            .findById(categoryId)
            .orElseThrow(() -> new ObjectNotFoundException("Category not found"));

    VotingSet votingSet =
        votingSetRepository
            .findByVotingUserAndVotingCategory(user, category)
            .orElseGet(() -> createVotingSet(user, category));

    return new VotingCategoryUserDetailDto(
        votingCategoryMapper.toBaseDto(category),
        new VotingCategoryUserStateDto(true, votingSet.isSubmitted()),
        votingCandidateMapper.toDto(category.getVotingCandidates()),
        votingSet.getPositionMap());
  }

  public void updateVotingPositions(
      Long categoryId, VotingUser user, VotingPositionMapDto positionMap) {
    VotingCategory category =
        votingCategoryRepository
            .findById(categoryId)
            .orElseThrow(() -> new ObjectNotFoundException("Category not found"));
    if (category.getSubmissionEnd().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Voting for this category has ended");
    }

    if (!positionMap.isValid(category.getVotingCandidates())) {
      throw new IllegalArgumentException("Invalid position map");
    }

    VotingSet votingSet =
        votingSetRepository
            .findByVotingUserAndVotingCategory(user, category)
            .orElseThrow(() -> new ObjectNotFoundException("Voting set not found"));
    if (votingSet.isSubmitted()) {
      throw new IllegalArgumentException("Voting set has already been submitted");
    }
    if (votingSet.isDisqualified()) {
      throw new IllegalArgumentException("Voting set has been disqualified");
    }

    votingSet.applyPositionMap(positionMap);
    votingSetRepository.save(votingSet);
  }

  public void submitVoting(Long categoryId, VotingUser user) {
    VotingCategory category =
        votingCategoryRepository
            .findById(categoryId)
            .orElseThrow(() -> new ObjectNotFoundException("Category not found"));

    if (category.getSubmissionStart().isAfter(LocalDateTime.now())) {
      throw new IllegalArgumentException("Voting for this category has not started yet");
    }
    if (category.getSubmissionEnd().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Voting for this category has already ended");
    }

    VotingSet votingSet =
        votingSetRepository
            .findByVotingUserAndVotingCategory(user, category)
            .orElseThrow(() -> new ObjectNotFoundException("Voting set not found"));
    if (votingSet.isSubmitted()) {
      throw new IllegalArgumentException("Voting set has already been submitted");
    }
    if (votingSet.isDisqualified()) {
      throw new IllegalArgumentException("Voting set has been disqualified");
    }

    if (!votingSet.getPositionMap().isSubmittable(category.getVotingCandidates())) {
      throw new IllegalArgumentException("Voting set is not valid and cannot be submitted");
    }

    votingSet.setSubmitted(true);
    votingSet.setSubmittedAt(LocalDateTime.now());
    votingSetRepository.save(votingSet);
  }
}
