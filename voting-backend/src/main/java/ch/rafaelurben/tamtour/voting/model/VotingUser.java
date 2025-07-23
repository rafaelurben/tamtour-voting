package ch.rafaelurben.tamtour.voting.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "voting_user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotingUser {
  @Id private Long id;

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

  @Column(name = "initial_registration_complete")
  private final boolean initialRegistrationComplete = false;

  @Column(name = "created_at")
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(name = "last_login_at")
  private LocalDateTime lastLoginAt;

  @Column(name = "blocked")
  private final boolean blocked = false;
}
