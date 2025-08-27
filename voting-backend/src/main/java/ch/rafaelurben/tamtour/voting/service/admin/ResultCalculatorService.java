package ch.rafaelurben.tamtour.voting.service.admin;

import ch.rafaelurben.tamtour.voting.dto.admin.VotingCategoryResultDto;
import ch.rafaelurben.tamtour.voting.dto.admin.VotingCategoryResultItemDto;
import ch.rafaelurben.tamtour.voting.mapper.VotingCandidateMapper;
import ch.rafaelurben.tamtour.voting.model.VotingCandidate;
import ch.rafaelurben.tamtour.voting.model.VotingCategory;
import ch.rafaelurben.tamtour.voting.model.VotingSet;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultCalculatorService {
  private final VotingCandidateMapper votingCandidateMapper;

  private long getPointsForPosition(Integer position) {
    return switch (position) {
      case 1 -> 12;
      case 2 -> 10;
      case 3 -> 8;
      case 4 -> 7;
      case 5 -> 6;
      case 6 -> 5;
      case 7 -> 4;
      case 8 -> 3;
      case 9 -> 2;
      case 10 -> 1;
      default -> 0;
    };
  }

  public VotingCategoryResultDto calculateResult(VotingCategory category) {
    Set<VotingCandidate> candidates = category.getVotingCandidates();
    Set<VotingSet> sets = category.getVotingSets();

    // Calculate points
    Map<Long, Long> candidatePointsMap = new HashMap<>();
    candidates.forEach(candidate -> candidatePointsMap.put(candidate.getId(), 0L));
    sets.stream()
        .filter(votingSet -> votingSet.isSubmitted() && !votingSet.isDisqualified())
        .flatMap(votingSet -> votingSet.getVotingPositions().stream())
        .forEach(
            position -> {
              Long candidateId = position.getVotingCandidateId();
              candidatePointsMap.compute(
                  candidateId,
                  (_, currentPoints) ->
                      currentPoints + getPointsForPosition(position.getPosition()));
            });

    List<Long> pointsList =
        candidatePointsMap.values().stream().sorted(Comparator.reverseOrder()).toList();
    // TODO: Handle ties in ranking
    VotingCategoryResultItemDto[] items =
        candidates.stream()
            .map(
                candidate -> {
                  long points = candidatePointsMap.get(candidate.getId());
                  int rank = pointsList.indexOf(points) + 1;
                  return new VotingCategoryResultItemDto(
                      votingCandidateMapper.toDto(candidate), points, rank);
                })
            .sorted(Comparator.comparingInt(VotingCategoryResultItemDto::rank))
            .toArray(VotingCategoryResultItemDto[]::new);

    return new VotingCategoryResultDto(
        category.getSubmissionEnd().isBefore(LocalDateTime.now()), category.getName(), items);
  }
}
