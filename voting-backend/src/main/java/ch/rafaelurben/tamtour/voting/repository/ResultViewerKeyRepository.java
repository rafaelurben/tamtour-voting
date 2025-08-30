package ch.rafaelurben.tamtour.voting.repository;

import ch.rafaelurben.tamtour.voting.model.ResultViewerKey;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultViewerKeyRepository extends JpaRepository<ResultViewerKey, Long> {
  Optional<ResultViewerKey> findByKey(UUID key);
}
