package ch.rafaelurben.tamtour.voting.repository;

import ch.rafaelurben.tamtour.voting.model.VotingCategory;
import ch.rafaelurben.tamtour.voting.model.VotingSet;
import ch.rafaelurben.tamtour.voting.model.VotingUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotingSetRepository extends JpaRepository<VotingSet, Long> {
  Optional<VotingSet> findByVotingUserAndVotingCategory(
      VotingUser votingUser, VotingCategory votingCategory);

  Optional<VotingSet> findByVotingCategoryIdAndId(Long categoryId, Long id);
}
