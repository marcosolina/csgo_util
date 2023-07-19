package com.ixigo.demmanager.models.svc.demdata;

import java.io.Serializable;
import java.util.ArrayList;
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
	private SvcMapFileStats mapStats = new SvcMapFileStats();
	private List<SvcPlayerStats> allPlayerStats = new ArrayList<>();
	private List<SvcRoundStats> allRoundStats = new ArrayList<>();
	private List<SvcPlayerRoundStats> allPlayerRoundStats = new ArrayList<>();
	private List<SvcRoundKillEvent> allRoundKillEvents = new ArrayList<>();
	private List<SvcRoundShotEvent> allRoundShotEvents = new ArrayList<>();
	private List<SvcRoundHitEvent> allRoundHitEvents = new ArrayList<>();
	private List<SvcRoundEvent> allRoundEvents = new ArrayList<>();
}
