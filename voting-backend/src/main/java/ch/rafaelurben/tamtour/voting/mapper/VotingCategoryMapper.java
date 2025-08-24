package ch.rafaelurben.tamtour.voting.mapper;

import ch.rafaelurben.tamtour.voting.dto.VotingCategoryBaseDto;
import ch.rafaelurben.tamtour.voting.dto.admin.VotingCategoryRequestDto;
import ch.rafaelurben.tamtour.voting.model.VotingCategory;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VotingCategoryMapper {
  VotingCategoryBaseDto toBaseDto(VotingCategory category);

  List<VotingCategoryBaseDto> toBaseDto(List<VotingCategory> categoryList);

  VotingCategory toEntity(VotingCategoryRequestDto categoryRequestDto);

  void updateEntityFromDto(@MappingTarget VotingCategory entity, VotingCategoryRequestDto dto);
}
