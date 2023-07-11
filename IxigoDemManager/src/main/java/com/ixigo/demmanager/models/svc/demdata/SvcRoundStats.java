package com.ixigo.demmanager.models.svc.demdata;

import java.util.List;

import com.ixigo.demmanager.enums.TeamNumber;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcRoundStats {
	private int roundNumber;
    private TeamNumber winnerSide;
    private int moneySpentTerrorists;
    private int moneySpentCT;
    private int deathsTerrorists;
    private int deathsCT;
    private int reasonEndRound;
    private int equipmentValueTerrorists;
    private int equipmentValueCT;
    private List<String> terroristSteamIDs;
    private List<String> ctSteamIDs;
}
