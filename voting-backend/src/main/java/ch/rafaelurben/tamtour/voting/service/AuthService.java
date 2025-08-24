package ch.rafaelurben.tamtour.voting.service;

import ch.rafaelurben.tamtour.voting.dto.UpdateMeDto;
import ch.rafaelurben.tamtour.voting.exceptions.ObjectNotFoundException;
import ch.rafaelurben.tamtour.voting.mapper.VotingUserMapper;
import ch.rafaelurben.tamtour.voting.model.VotingUser;
import ch.rafaelurben.tamtour.voting.repository.VotingUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final VotingUserRepository votingUserRepository;
  private final VotingUserMapper votingUserMapper;

  public VotingUser getUserById(Long userId) {
    return votingUserRepository
        .findById(userId)
        .orElseThrow(() -> new ObjectNotFoundException("Benutzer nicht gefunden"));
  }

  public VotingUser updateUser(VotingUser user, UpdateMeDto updateMeDto) {
    votingUserMapper.update(user, updateMeDto);
    user.setInitialRegistrationComplete(true);
    return votingUserRepository.save(user);
  }
}
