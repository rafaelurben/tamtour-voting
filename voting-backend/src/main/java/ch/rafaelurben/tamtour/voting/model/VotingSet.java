package ch.rafaelurben.tamtour.voting.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "voting_set")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotingSet {
  @Id private Long id;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private VotingCategory votingCategory;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private VotingUser votingUser;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<VotingPosition> votingPositions;

  @Column(name = "created_at")
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(name = "modified_at")
  @LastModifiedDate
  private LocalDateTime modifiedAt;

  @Column(name = "submitted_at")
  private LocalDateTime submittedAt;

  @Column(name = "submitted")
  private final boolean submitted = false;

  @Column(name = "disqualified")
  private final boolean disqualified = false;
}
