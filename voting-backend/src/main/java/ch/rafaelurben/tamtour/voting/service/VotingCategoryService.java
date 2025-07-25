package ch.rafaelurben.tamtour.voting.service;

import ch.rafaelurben.tamtour.voting.dto.VotingCategoryUserOverviewDto;
import ch.rafaelurben.tamtour.voting.dto.VotingCategoryUserStateDto;
import ch.rafaelurben.tamtour.voting.mapper.VotingCategoryMapper;
import ch.rafaelurben.tamtour.voting.model.VotingCategory;
import ch.rafaelurben.tamtour.voting.model.VotingSet;
import ch.rafaelurben.tamtour.voting.model.VotingUser;
import ch.rafaelurben.tamtour.voting.repository.VotingCategoryRepository;
import ch.rafaelurben.tamtour.voting.repository.VotingSetRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VotingCategoryService {
  private final VotingCategoryMapper votingCategoryMapper;
  private final VotingCategoryRepository votingCategoryRepository;
  private final VotingSetRepository votingSetRepository;

  public List<VotingCategoryUserOverviewDto> getCategoryOverviewForUser(VotingUser user) {
    List<VotingCategory> categories = votingCategoryRepository.findAll();

    return categories.stream()
        .map(
            category -> {
              Optional<VotingSet> votingSet =
                  votingSetRepository.findByVotingUserAndVotingCategory(user, category);
              boolean exists = votingSet.isPresent();
              boolean submitted = exists && votingSet.get().isSubmitted();

              return new VotingCategoryUserOverviewDto(
                  votingCategoryMapper.toBaseDto(category),
                  new VotingCategoryUserStateDto(exists, submitted));
            })
        .toList();
  }
}
