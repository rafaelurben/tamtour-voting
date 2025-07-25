package ch.rafaelurben.tamtour.voting.repository;

import ch.rafaelurben.tamtour.voting.model.VotingCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotingCategoryRepository extends JpaRepository<VotingCategory, Long> {}
