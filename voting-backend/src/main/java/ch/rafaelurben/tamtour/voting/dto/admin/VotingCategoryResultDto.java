package ch.rafaelurben.tamtour.voting.dto.admin;

public record VotingCategoryResultDto(boolean ended, VotingCategoryResultItemDto[] items) {}
