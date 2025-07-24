package ch.rafaelurben.tamtour.voting.dto;

import java.time.LocalDateTime;

public record VotingUserDto(
    Long id,
    String firstName,
    String lastName,
    String accountEmail,
    String accountName,
    String pictureLink,
    boolean initialRegistrationComplete,
    LocalDateTime createdAt,
    LocalDateTime lastLoginAt,
    boolean blocked) {}
