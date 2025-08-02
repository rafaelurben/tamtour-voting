package ch.rafaelurben.tamtour.voting.mapper;

import ch.rafaelurben.tamtour.voting.dto.UpdateMeDto;
import ch.rafaelurben.tamtour.voting.dto.VotingUserDto;
import ch.rafaelurben.tamtour.voting.model.VotingUser;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VotingUserMapper {
  VotingUserDto toDto(VotingUser user);

  List<VotingUserDto> toDto(List<VotingUser> users);

  void update(@MappingTarget VotingUser user, UpdateMeDto dto);
}
