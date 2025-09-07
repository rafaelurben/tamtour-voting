package ch.rafaelurben.tamtour.voting.service.admin;

import ch.rafaelurben.tamtour.voting.dto.VotingCandidateDto;
import ch.rafaelurben.tamtour.voting.dto.admin.VotingCategoryResultDto;
import ch.rafaelurben.tamtour.voting.dto.admin.VotingCategoryResultItemDto;
import ch.rafaelurben.tamtour.voting.mapper.VotingCandidateMapper;
import ch.rafaelurben.tamtour.voting.model.VotingCategory;
import jakarta.annotation.Nonnull;
import java.time.OffsetDateTime;
import java.util.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultCalculatorService {

  private final VotingCandidateMapper votingCandidateMapper;

  @Getter // for Jackson
  @RequiredArgsConstructor
  public static class VotingResultItem implements Comparable<VotingResultItem> {
    private static final int MAX_POSITION_RELEVANT_FOR_ORDERING = 10;

    private final VotingCandidateDto candidateDto;
    private final int[] positionRankCounts = new int[MAX_POSITION_RELEVANT_FOR_ORDERING];
    private long points;
    private int rank;

    /**
     * Adds points for the given position and updates the count of positions for tie-breaking.
     *
     * @param position the position to add from a VotingPosition (1-based)
     */
    public void addPosition(int position) {
      points += getPointsForPosition(position);
      if (position <= MAX_POSITION_RELEVANT_FOR_ORDERING) {
        positionRankCounts[position - 1]++;
      }
    }

    /**
     * Compares first by points (descending), then by number of first places (descending), then by
     * number of second places (descending), etc.
     *
     * @param o the other VotingResultItem to compare to
     * @return comparison result
     */
    @Override
    public int compareTo(@Nonnull VotingResultItem o) {
      int result = Long.compare(o.points, this.points);
      if (result != 0) {
        return result;
      }
      for (int i = 0; i < MAX_POSITION_RELEVANT_FOR_ORDERING; i++) {
        result = Integer.compare(o.positionRankCounts[i], this.positionRankCounts[i]);
        if (result != 0) {
          return result;
        }
      }
      return 0;
    }

    private static long getPointsForPosition(Integer position) {
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
  }

  /**
   * Calculates the points for each candidate in the given voting category.
   *
   * @param category the voting category
   * @return a collection of VotingResultItem, one for each candidate
   */
  private Collection<VotingResultItem> calculatePoints(VotingCategory category) {
    Map<Long, VotingResultItem> candidateItemMap = new HashMap<>();
    category
        .getVotingCandidates()
        .forEach(
            candidate ->
                candidateItemMap.put(
                    candidate.getId(),
                    new VotingResultItem(votingCandidateMapper.toDto(candidate))));
    category.getVotingSets().stream()
        .filter(votingSet -> votingSet.isSubmitted() && !votingSet.isDisqualified())
        .flatMap(votingSet -> votingSet.getVotingPositions().stream())
        .forEach(
            position -> {
              Long candidateId = position.getVotingCandidateId();
              candidateItemMap.get(candidateId).addPosition(position.getPosition());
            });

    return candidateItemMap.values();
  }

  /**
   * Calculates the ranks for the given items with points. The items are sorted by points and
   * tie-breaking rules, and ranks are assigned starting from 1. Items with the same points and
   * tie-breaking criteria will receive a random order among them.
   *
   * @param itemsWithPoints the items with points
   * @return an ordered list of VotingResultItem with ranks assigned
   */
  private List<VotingResultItem> calculateRanks(Collection<VotingResultItem> itemsWithPoints) {
    List<VotingResultItem> resultItems = new ArrayList<>(itemsWithPoints);
    // Shuffle to randomize order of items with compareTo == 0
    Collections.shuffle(resultItems);
    // Sort by natural order (compareTo) using stable sort
    resultItems = resultItems.stream().sorted().toList();
    for (int i = 0; i < resultItems.size(); i++) {
      resultItems.get(i).rank = i + 1;
    }
    return resultItems;
  }

  /**
   * Calculates the result data (points and ranks) for the given voting category.
   *
   * @param category the voting category
   * @return an ordered list of VotingResultItem with points and ranks assigned
   */
  public List<VotingResultItem> calculateResultData(VotingCategory category) {
    return calculateRanks(calculatePoints(category));
  }

  /**
   * Calculates the result for the given voting category.
   *
   * @param category the voting category
   * @return the result DTO
   */
  public VotingCategoryResultDto calculateResult(VotingCategory category) {
    List<VotingResultItem> resultItemsOrdered = calculateResultData(category);

    return new VotingCategoryResultDto(
        category.getSubmissionEnd().isBefore(OffsetDateTime.now()),
        category.getName(),
        resultItemsOrdered.stream()
            .map(item -> new VotingCategoryResultItemDto(item.candidateDto, item.points, item.rank))
            .toArray(VotingCategoryResultItemDto[]::new));
  }
}
