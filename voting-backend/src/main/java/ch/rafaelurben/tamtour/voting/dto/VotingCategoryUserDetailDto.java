package ch.rafaelurben.tamtour.voting.dto;

import java.util.Collection;

public record VotingCategoryUserDetailDto(
    VotingCategoryBaseDto category,
    VotingCategoryUserStateDto userState,
    Collection<VotingCandidateDto> candidates,
    VotingPositionMapDto positionMap) {}
