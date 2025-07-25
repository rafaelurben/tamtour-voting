package ch.rafaelurben.tamtour.voting.dto;

import java.time.LocalDateTime;

public record VotingCategoryBaseDto(
    Long id,
    String name,
    LocalDateTime votingStart,
    LocalDateTime submissionStart,
    LocalDateTime submissionEnd) {}
