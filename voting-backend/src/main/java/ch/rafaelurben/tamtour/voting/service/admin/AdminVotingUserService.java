package ch.rafaelurben.tamtour.voting.service.admin;

import ch.rafaelurben.tamtour.voting.dto.*;
import ch.rafaelurben.tamtour.voting.mapper.VotingUserMapper;
import ch.rafaelurben.tamtour.voting.repository.VotingUserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminVotingUserService {
  private final VotingUserRepository votingUserRepository;
  private final VotingUserMapper votingUserMapper;

  public List<VotingUserDto> getAllUsers() {
    return votingUserMapper.toDto(votingUserRepository.findAll());
  }
}
