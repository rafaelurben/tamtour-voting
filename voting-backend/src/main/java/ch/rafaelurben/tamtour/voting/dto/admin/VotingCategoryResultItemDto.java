package ch.rafaelurben.tamtour.voting.dto.admin;

import ch.rafaelurben.tamtour.voting.dto.VotingCandidateDto;

public record VotingCategoryResultItemDto(VotingCandidateDto candidate, long points, int rank) {}
