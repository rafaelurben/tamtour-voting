package ch.rafaelurben.tamtour.voting.mapper;

import ch.rafaelurben.tamtour.voting.dto.UpdateMeDto;
import ch.rafaelurben.tamtour.voting.dto.VotingUserDto;
import ch.rafaelurben.tamtour.voting.model.VotingUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VotingUserMapper {
  VotingUserDto toDto(VotingUser user);

  void update(@MappingTarget VotingUser user, UpdateMeDto dto);
}
