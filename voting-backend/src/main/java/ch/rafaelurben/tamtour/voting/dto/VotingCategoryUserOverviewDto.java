package ch.rafaelurben.tamtour.voting.dto;

public record VotingCategoryUserOverviewDto(
    VotingCategoryBaseDto category, VotingCategoryUserStateDto userState) {}
