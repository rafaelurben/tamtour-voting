package ch.rafaelurben.tamtour.voting.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "voting_position")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotingPosition {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "candidate_id", insertable = false, updatable = false)
  private Long votingCandidateId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "candidate_id")
  private VotingCandidate votingCandidate;

  @ManyToOne
  @JoinColumn(name = "set_id")
  private VotingSet votingSet;

  @Column(name = "position")
  @Min(1)
  @Builder.Default
  private Integer position = null;

  public int getPoints() {
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
