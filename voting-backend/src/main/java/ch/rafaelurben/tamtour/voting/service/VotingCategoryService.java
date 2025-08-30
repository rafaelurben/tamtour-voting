package ch.rafaelurben.tamtour.voting.service;

import ch.rafaelurben.tamtour.voting.dto.VotingCategoryUserDetailDto;
import ch.rafaelurben.tamtour.voting.dto.VotingCategoryUserOverviewDto;
import ch.rafaelurben.tamtour.voting.dto.VotingCategoryUserStateDto;
import ch.rafaelurben.tamtour.voting.dto.VotingPositionMapDto;
import ch.rafaelurben.tamtour.voting.exceptions.InvalidStateException;
import ch.rafaelurben.tamtour.voting.exceptions.ObjectNotFoundException;
import ch.rafaelurben.tamtour.voting.mapper.VotingCandidateMapper;
import ch.rafaelurben.tamtour.voting.mapper.VotingCategoryMapper;
import ch.rafaelurben.tamtour.voting.model.VotingCategory;
import ch.rafaelurben.tamtour.voting.model.VotingPosition;
import ch.rafaelurben.tamtour.voting.model.VotingSet;
import ch.rafaelurben.tamtour.voting.model.VotingUser;
import ch.rafaelurben.tamtour.voting.repository.VotingCategoryRepository;
import ch.rafaelurben.tamtour.voting.repository.VotingSetRepository;
import java.time.OffsetDateTime;
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
              boolean disqualified = exists && votingSet.get().isDisqualified();

              return new VotingCategoryUserOverviewDto(
                  votingCategoryMapper.toBaseDto(category),
                  new VotingCategoryUserStateDto(exists, submitted, disqualified));
            })
        .toList();
  }

  private VotingSet createVotingSet(VotingUser user, VotingCategory category) {
    VotingSet votingSet =
        votingSetRepository.save(
            VotingSet.builder().votingUser(user).votingCategory(category).build());
    votingSet.setVotingPositions(
        category.getVotingCandidates().stream()
            .map(
                candidate ->
                    VotingPosition.builder()
                        .votingSet(votingSet)
                        .votingCandidate(candidate)
                        .votingCandidateId(candidate.getId())
                        .position(null)
                        .build())
            .collect(Collectors.toSet()));
    return votingSetRepository.save(votingSet);
  }

  private VotingCategory getCategory(Long categoryId) {
    return votingCategoryRepository
        .findById(categoryId)
        .orElseThrow(
            () -> new ObjectNotFoundException("Kategorie #" + categoryId + " nicht gefunden."));
  }

  public VotingCategoryUserDetailDto getCategoryForUser(Long categoryId, VotingUser user) {
    VotingCategory category = getCategory(categoryId);
    if (category.getVotingStart().isAfter(OffsetDateTime.now())) {
      throw new InvalidStateException("Das Voting für diese Kategorie hat noch nicht begonnen.");
    }

    VotingSet votingSet;
    if (category.getSubmissionEnd().isBefore(OffsetDateTime.now())) {
      votingSet =
          votingSetRepository
              .findByVotingUserAndVotingCategory(user, category)
              .orElseThrow(() -> new InvalidStateException("Voting bereits beendet."));
    } else {
      votingSet =
          votingSetRepository
              .findByVotingUserAndVotingCategory(user, category)
              .orElseGet(() -> createVotingSet(user, category));
    }

    return new VotingCategoryUserDetailDto(
        votingCategoryMapper.toBaseDto(category),
        new VotingCategoryUserStateDto(true, votingSet.isSubmitted(), votingSet.isDisqualified()),
        votingCandidateMapper.toDto(category.getVotingCandidates()),
        votingSet.getPositionMap());
  }

  public void updateVotingPositions(
      Long categoryId, VotingUser user, VotingPositionMapDto positionMap) {
    VotingCategory category = getCategory(categoryId);
    if (category.getVotingStart().isAfter(OffsetDateTime.now())) {
      throw new InvalidStateException("Das Voting für diese Kategorie hat noch nicht begonnen.");
    }
    if (category.getSubmissionEnd().isBefore(OffsetDateTime.now())) {
      throw new InvalidStateException("Die Einsendephase für diese Kategorie ist bereits beendet.");
    }

    if (!positionMap.isValid(category.getVotingCandidates())) {
      throw new IllegalArgumentException("Ungültige Positionen. Bitte lade die Seite neu.");
    }

    VotingSet votingSet =
        votingSetRepository
            .findByVotingUserAndVotingCategory(user, category)
            .orElseThrow(() -> new ObjectNotFoundException("Kein Voting-Set gefunden."));
    if (votingSet.isSubmitted()) {
      throw new InvalidStateException("Deine Rangliste wurde bereits eingereicht.");
    }
    if (votingSet.isDisqualified()) {
      throw new InvalidStateException("Deine Teilnahme in dieser Kategorie wurde disqualifiziert.");
    }

    votingSet.applyPositionMap(positionMap);
    votingSetRepository.save(votingSet);
  }

  public void submitVoting(Long categoryId, VotingUser user) {
    VotingCategory category = getCategory(categoryId);
    if (category.getSubmissionStart().isAfter(OffsetDateTime.now())) {
      throw new InvalidStateException(
          "Die Einsendephase für diese Kategorie hat noch nicht begonnen.");
    }
    if (category.getSubmissionEnd().isBefore(OffsetDateTime.now())) {
      throw new InvalidStateException("Die Einsendephase für diese Kategorie ist bereits beendet.");
    }

    VotingSet votingSet =
        votingSetRepository
            .findByVotingUserAndVotingCategory(user, category)
            .orElseThrow(() -> new ObjectNotFoundException("Kein Voting-Set gefunden."));
    if (votingSet.isSubmitted()) {
      throw new InvalidStateException("Deine Rangliste wurde bereits eingereicht.");
    }
    if (votingSet.isDisqualified()) {
      throw new InvalidStateException("Deine Teilnahme in dieser Kategorie wurde disqualifiziert.");
    }

    if (!votingSet.getPositionMap().isSubmittable(category.getVotingCandidates())) {
      throw new InvalidStateException(
          "Die aktuell gespeicherte Rangliste kann nicht eingereicht werden, da sie nicht gültig"
              + " ist. Bitte lade die Seite neu.");
    }

    votingSet.setSubmitted(true);
    votingSet.setSubmittedAt(OffsetDateTime.now());
    votingSetRepository.save(votingSet);
  }
}
