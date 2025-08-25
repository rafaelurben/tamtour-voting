package ch.rafaelurben.tamtour.voting.service.admin;

import ch.rafaelurben.tamtour.voting.dto.admin.ResultViewerKeyDto;
import ch.rafaelurben.tamtour.voting.dto.admin.ResultViewerKeyRequestDto;
import ch.rafaelurben.tamtour.voting.exceptions.ObjectNotFoundException;
import ch.rafaelurben.tamtour.voting.mapper.ResultViewerKeyMapper;
import ch.rafaelurben.tamtour.voting.model.ResultViewerKey;
import ch.rafaelurben.tamtour.voting.repository.ResultViewerKeyRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminViewerKeyService {
  private final ResultViewerKeyMapper resultViewerKeyMapper;
  private final ResultViewerKeyRepository resultViewerKeyRepository;

  public List<ResultViewerKeyDto> getAllKeys() {
    return resultViewerKeyMapper.toDto(resultViewerKeyRepository.findAll());
  }

  public ResultViewerKeyDto createKey(ResultViewerKeyRequestDto dto) {
    ResultViewerKey resultViewerKey =
        ResultViewerKey.builder().name(dto.name()).key(UUID.randomUUID()).build();
    resultViewerKey = resultViewerKeyRepository.save(resultViewerKey);
    return resultViewerKeyMapper.toDto(resultViewerKey);
  }

  public ResultViewerKeyDto updateKey(Long id, ResultViewerKeyRequestDto dto) {
    ResultViewerKey resultViewerKey =
        resultViewerKeyRepository
            .findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("ResultViewerKey not found"));
    resultViewerKey.setName(dto.name());
    resultViewerKey = resultViewerKeyRepository.save(resultViewerKey);
    return resultViewerKeyMapper.toDto(resultViewerKey);
  }

  public void deleteKey(Long id) {
    if (!resultViewerKeyRepository.existsById(id)) {
      throw new ObjectNotFoundException("ResultViewerKey not found");
    }
    resultViewerKeyRepository.deleteById(id);
  }
}
