package ch.rafaelurben.tamtour.voting.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Table(name = "voting_candidate")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotingCandidate {
  @Id private Long id;

  @Column(name = "name")
  private String name;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private VotingCategory votingCategory;
}
