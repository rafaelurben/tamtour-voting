package ch.rafaelurben.tamtour.voting.dto;

import java.util.List;

public record WhoamiDto(VotingUserDto user, List<String> roles, boolean isAdmin) {}
