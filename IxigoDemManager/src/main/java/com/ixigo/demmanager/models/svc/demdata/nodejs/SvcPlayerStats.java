package com.ixigo.demmanager.models.svc.demdata.nodejs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcPlayerStats {
	private String userName;
    private String steamID;
    private int matchId;
    private int score;
}
