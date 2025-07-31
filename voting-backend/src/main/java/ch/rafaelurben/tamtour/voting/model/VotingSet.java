package ch.rafaelurben.tamtour.voting.model;

import ch.rafaelurben.tamtour.voting.dto.VotingPositionMapDto;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "voting_set")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotingSet {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private VotingCategory votingCategory;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private VotingUser votingUser;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "votingSet")
  private Set<VotingPosition> votingPositions;

  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "modified_at")
  @UpdateTimestamp
  private LocalDateTime modifiedAt;

  @Column(name = "submitted_at")
  private LocalDateTime submittedAt;

  @Column(name = "submitted")
  @Builder.Default
  private boolean submitted = false;

  @Column(name = "disqualified")
  @Builder.Default
  private boolean disqualified = false;

  public VotingPositionMapDto getPositionMap() {
    return votingPositions.stream()
        .collect(
            VotingPositionMapDto::new,
            (m, v) -> m.put(v.getVotingCandidateId(), v.getPosition()),
            VotingPositionMapDto::putAll);
  }

  public void applyPositionMap(VotingPositionMapDto positionMap) {
    votingPositions.forEach(
        votingPosition -> {
          Integer position = positionMap.get(votingPosition.getVotingCandidateId());
          votingPosition.setPosition(position);
        });
  }
}
