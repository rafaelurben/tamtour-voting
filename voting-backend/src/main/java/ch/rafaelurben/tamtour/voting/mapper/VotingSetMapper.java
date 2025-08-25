package ch.rafaelurben.tamtour.voting.mapper;

import ch.rafaelurben.tamtour.voting.dto.admin.VotingSetDto;
import ch.rafaelurben.tamtour.voting.dto.admin.VotingSetUpdateDto;
import ch.rafaelurben.tamtour.voting.model.VotingSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
    componentModel = "spring",
    uses = {VotingUserMapper.class})
public interface VotingSetMapper {
  VotingSetDto toDto(VotingSet votingSet);

  Set<VotingSetDto> toDto(Set<VotingSet> votingSets);

  void updateEntityFromDto(@MappingTarget VotingSet entity, VotingSetUpdateDto dto);
}
