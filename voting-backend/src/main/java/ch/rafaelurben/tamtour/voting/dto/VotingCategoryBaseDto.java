package ch.rafaelurben.tamtour.voting.dto;

import java.time.OffsetDateTime;

public record VotingCategoryBaseDto(
    Long id,
    String name,
    OffsetDateTime votingStart,
    OffsetDateTime submissionStart,
    OffsetDateTime submissionEnd) {}
