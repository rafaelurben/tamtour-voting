package ch.rafaelurben.tamtour.voting.repository;

import ch.rafaelurben.tamtour.voting.model.VotingUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VotingUserRepository extends JpaRepository<VotingUser, Long> {
  Optional<VotingUser> findBySub(String sub);
}
