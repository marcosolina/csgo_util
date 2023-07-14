package com.ixigo.demmanager.models.svc.demdata;

import java.io.Serializable;
import java.util.List;

import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcMapFileStats;
import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcPlayerRoundStats;
import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcPlayerStats;
import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcRoundEvent;
import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcRoundHitEvent;
import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcRoundKillEvent;
import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcRoundShotEvent;
import com.ixigo.demmanager.models.svc.demdata.nodejs.SvcRoundStats;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcNodeJsParseOutput implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<SvcPlayerStats> allPlayerStats;
	private SvcMapFileStats mapStats;
	private List<SvcRoundStats> allRoundStats;
	private List<SvcPlayerRoundStats> allPlayerRoundStats;
	private List<SvcRoundKillEvent> allRoundKillEvents;
	private List<SvcRoundShotEvent> allRoundShotEvents;
	private List<SvcRoundHitEvent> allRoundHitEvents;
	private List<SvcRoundEvent> allRoundEvents;
}
