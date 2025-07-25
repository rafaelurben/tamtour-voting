package ch.rafaelurben.tamtour.voting.repository;

import ch.rafaelurben.tamtour.voting.model.VotingCategory;
import ch.rafaelurben.tamtour.voting.model.VotingSet;
import ch.rafaelurben.tamtour.voting.model.VotingUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VotingSetRepository extends JpaRepository<VotingSet, Long> {
  Optional<VotingSet> findByVotingUserAndVotingCategory(
      VotingUser votingUser, VotingCategory votingCategory);
}
