package ch.rafaelurben.tamtour.voting.dto.admin;

import ch.rafaelurben.tamtour.voting.dto.VotingPositionMapDto;
import ch.rafaelurben.tamtour.voting.dto.VotingUserDto;
import java.time.OffsetDateTime;

public record VotingSetDto(
    Long id,
    VotingUserDto votingUser,
    OffsetDateTime createdAt,
    OffsetDateTime modifiedAt,
    OffsetDateTime submittedAt,
    boolean submitted,
    boolean disqualified,
    VotingPositionMapDto positionMap) {}
