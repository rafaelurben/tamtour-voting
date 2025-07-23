package ch.rafaelurben.tamtour.voting.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "result_viewer_key")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultViewerKey {
  @Id private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "key", unique = true)
  private UUID key;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private VotingCategory votingCategory;
}
