package ch.rafaelurben.tamtour.voting.repository;

import ch.rafaelurben.tamtour.voting.model.VotingUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotingUserRepository extends JpaRepository<VotingUser, Long> {
  Optional<VotingUser> findBySub(String sub);
}
