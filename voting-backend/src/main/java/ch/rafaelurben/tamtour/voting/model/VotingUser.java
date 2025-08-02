package ch.rafaelurben.tamtour.voting.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "voting_user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotingUser implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  /** First name, prefilled from Google. */
  @Column(name = "first_name")
  private String firstName;

  /** Last name, prefilled from Google. */
  @Column(name = "last_name")
  private String lastName;

  /** Account email, fixed from Google. */
  @Column(name = "account_email")
  private String accountEmail;

  /** Full name, fixed from Google. */
  @Column(name = "account_name")
  private String accountName;

  /** Profile picture link; fixed from Google. */
  @Column(name = "picture_link")
  private String pictureLink;

  /** Profile picture link; fixed from Google. */
  @Column(name = "sub", unique = true)
  private String sub;

  @Column(name = "initial_registration_complete")
  @Builder.Default
  private boolean initialRegistrationComplete = false;

  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "last_login_at")
  private LocalDateTime lastLoginAt;

  @Column(name = "blocked")
  @Builder.Default
  private boolean blocked = false;
}
