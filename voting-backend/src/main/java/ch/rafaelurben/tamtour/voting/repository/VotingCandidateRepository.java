package ch.rafaelurben.tamtour.voting.repository;

import ch.rafaelurben.tamtour.voting.model.VotingCandidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotingCandidateRepository extends JpaRepository<VotingCandidate, Long> {}
