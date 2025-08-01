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
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "start_number")
  private String startNumber;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private VotingCategory votingCategory;
}
