package ch.rafaelurben.tamtour.voting.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "voting_category")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotingCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "voting_start")
  private LocalDateTime votingStart;

  @Column(name = "submission_start")
  private LocalDateTime submissionStart;

  @Column(name = "submission_end")
  private LocalDateTime submissionEnd;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "votingCategory")
  private Set<VotingCandidate> votingCandidates;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "votingCategory")
  private Set<VotingSet> votingSets;

  public Map<Long, Integer> calculatePoints() {
    Map<Long, Integer> candidatePointsMap = new HashMap<>();
    votingCandidates.forEach(candidate -> candidatePointsMap.put(candidate.getId(), 0));
    votingSets.stream()
        .filter(votingSet -> votingSet.isSubmitted() && !votingSet.isDisqualified())
        .flatMap(votingSet -> votingSet.getVotingPositions().stream())
        .forEach(
            position -> {
              Long candidateId = position.getVotingCandidateId();
              candidatePointsMap.compute(
                  candidateId, (_, currentPoints) -> currentPoints + position.getPoints());
            });
    return candidatePointsMap;
  }
}
