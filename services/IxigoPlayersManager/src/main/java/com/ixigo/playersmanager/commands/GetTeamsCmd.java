package com.ixigo.playersmanager.commands;

import java.util.List;

import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;
import com.ixigo.playersmanagercontract.enums.ScoreType;
import com.ixigo.playersmanagercontract.models.rest.RestTeams;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetTeamsCmd implements WebCommandRequest<RestTeams> {
	private Integer numberOfMatches;
	private List<String> steamIDs;
	private Double penaltyWeigth;
	private ScoreType partitionScore;
}
