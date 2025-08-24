package ch.rafaelurben.tamtour.voting.mapper;

import ch.rafaelurben.tamtour.voting.dto.VotingCandidateDto;
import ch.rafaelurben.tamtour.voting.dto.admin.VotingCandidateRequestDto;
import ch.rafaelurben.tamtour.voting.model.VotingCandidate;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VotingCandidateMapper {
  VotingCandidateDto toDto(VotingCandidate candidate);

  Set<VotingCandidateDto> toDto(Set<VotingCandidate> candidates);

  VotingCandidate toEntity(VotingCandidateRequestDto candidateDto);

  void updateEntityFromDto(@MappingTarget VotingCandidate entity, VotingCandidateRequestDto dto);
}
