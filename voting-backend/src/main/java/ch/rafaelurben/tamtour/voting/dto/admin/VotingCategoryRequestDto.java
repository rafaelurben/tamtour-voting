package ch.rafaelurben.tamtour.voting.dto.admin;

import java.time.LocalDateTime;

public record VotingCategoryRequestDto(
    String name,
    LocalDateTime votingStart,
    LocalDateTime submissionStart,
    LocalDateTime submissionEnd) {}
