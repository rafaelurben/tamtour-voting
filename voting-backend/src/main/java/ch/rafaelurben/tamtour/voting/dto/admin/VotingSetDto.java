package ch.rafaelurben.tamtour.voting.dto.admin;

import ch.rafaelurben.tamtour.voting.dto.VotingPositionMapDto;
import ch.rafaelurben.tamtour.voting.dto.VotingUserDto;

import java.time.LocalDateTime;

public record VotingSetDto(
    Long id,
    VotingUserDto votingUser,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    LocalDateTime submittedAt,
    boolean submitted,
    boolean disqualified,
    VotingPositionMapDto positionMap) {}
