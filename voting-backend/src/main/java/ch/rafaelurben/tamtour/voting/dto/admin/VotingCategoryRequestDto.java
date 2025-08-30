package ch.rafaelurben.tamtour.voting.dto.admin;

import java.time.OffsetDateTime;

public record VotingCategoryRequestDto(
    String name,
    OffsetDateTime votingStart,
    OffsetDateTime submissionStart,
    OffsetDateTime submissionEnd) {}
