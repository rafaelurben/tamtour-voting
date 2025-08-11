package ch.rafaelurben.tamtour.voting.service.admin;

import ch.rafaelurben.tamtour.voting.dto.*;
import ch.rafaelurben.tamtour.voting.exceptions.ObjectNotFoundException;
import ch.rafaelurben.tamtour.voting.mapper.VotingUserMapper;
import ch.rafaelurben.tamtour.voting.model.VotingUser;
import ch.rafaelurben.tamtour.voting.repository.VotingUserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminVotingUserService {
  private final VotingUserRepository votingUserRepository;
  private final VotingUserMapper votingUserMapper;
  private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;

  public List<VotingUserDto> getAllUsers() {
    return votingUserMapper.toDto(votingUserRepository.findAll());
  }

  private VotingUser getUserById(Long userId) {
    return votingUserRepository
        .findById(userId)
        .orElseThrow(
            () -> new ObjectNotFoundException("Benutzer nicht gefunden mit id: " + userId));
  }

  private void invalidateAllUserSessions(String email) {
    sessionRepository
        .findByPrincipalName(email)
        .values()
        .forEach(session -> sessionRepository.deleteById(session.getId()));
  }

  public VotingUserDto blockUser(Long userId) {
    VotingUser user = getUserById(userId);
    user.setBlocked(true);
    user = votingUserRepository.save(user);
    invalidateAllUserSessions(user.getAccountEmail());
    return votingUserMapper.toDto(user);
  }
}
