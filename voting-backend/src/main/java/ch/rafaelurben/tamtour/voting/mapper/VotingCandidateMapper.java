package ch.rafaelurben.tamtour.voting.mapper;

import ch.rafaelurben.tamtour.voting.dto.VotingCandidateDto;
import ch.rafaelurben.tamtour.voting.model.VotingCandidate;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VotingCandidateMapper {
  Set<VotingCandidateDto> toDto(Set<VotingCandidate> candidates);
}
