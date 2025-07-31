package ch.rafaelurben.tamtour.voting.dto;

import ch.rafaelurben.tamtour.voting.model.VotingCandidate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VotingPositionMapDto extends HashMap<Long, Integer> {
  public static VotingPositionMapDto fromMap(Map<Long, Integer> src) {
    VotingPositionMapDto dto = new VotingPositionMapDto();
    dto.putAll(src);
    return dto;
  }

  public boolean isValid(Collection<VotingCandidate> candidates) {
    // Check if sizes match
    if (candidates.size() != this.size()) {
      return false;
    }
    // Check if all candidate IDs are present
    if (!this.keySet().containsAll(candidates.stream().map(VotingCandidate::getId).toList())) {
      return false;
    }
    // Check if all positions are within the valid range
    if (this.values().stream().filter(Objects::nonNull).anyMatch(v -> v < 1 || v > this.size())) {
      return false;
    }
    // Check if all positions are unique
    long nonNullValuesCount = this.values().stream().filter(Objects::nonNull).count();
    long uniquePositionsCount = this.values().stream().filter(Objects::nonNull).distinct().count();
    return nonNullValuesCount == uniquePositionsCount;
  }

  public boolean isSubmittable(Collection<VotingCandidate> candidates) {
    return this.values().stream().noneMatch(Objects::isNull) && this.isValid(candidates);
  }
}
