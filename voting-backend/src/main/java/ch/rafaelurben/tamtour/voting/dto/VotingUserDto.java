package ch.rafaelurben.tamtour.voting.dto;

import java.time.OffsetDateTime;

public record VotingUserDto(
    Long id,
    String firstName,
    String lastName,
    String accountEmail,
    String accountName,
    String pictureLink,
    boolean initialRegistrationComplete,
    OffsetDateTime createdAt,
    OffsetDateTime lastLoginAt,
    boolean blocked) {}
