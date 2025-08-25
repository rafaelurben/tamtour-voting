package ch.rafaelurben.tamtour.voting.mapper;

import ch.rafaelurben.tamtour.voting.dto.admin.ResultViewerKeyDto;
import ch.rafaelurben.tamtour.voting.model.ResultViewerKey;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResultViewerKeyMapper {
  ResultViewerKeyDto toDto(ResultViewerKey resultViewerKey);

  List<ResultViewerKeyDto> toDto(List<ResultViewerKey> resultViewerKeys);
}
