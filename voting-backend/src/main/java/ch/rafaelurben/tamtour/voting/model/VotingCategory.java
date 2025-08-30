package ch.rafaelurben.tamtour.voting.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import java.time.OffsetDateTime;
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
  private OffsetDateTime votingStart;

  @Column(name = "submission_start")
  private OffsetDateTime submissionStart;

  @Column(name = "submission_end")
  private OffsetDateTime submissionEnd;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "votingCategory")
  private Set<VotingCandidate> votingCandidates;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "votingCategory")
  private Set<VotingSet> votingSets;
}
