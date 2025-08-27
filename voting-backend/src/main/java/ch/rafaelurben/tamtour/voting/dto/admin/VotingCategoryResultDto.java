package ch.rafaelurben.tamtour.voting.dto.admin;

public record VotingCategoryResultDto(boolean ended, String title, VotingCategoryResultItemDto[] items) {}
