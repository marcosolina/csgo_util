package com.ixigo.demmanager.models.svc.demdata.nodejs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ixigo.demmanager.models.svc.demdata.data.SvcMapStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundEvent;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundHitEvent;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundKillEvent;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundShotEvent;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundStats;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcNodeJsParseOutput implements Serializable {
	private static final long serialVersionUID = 1L;
	private SvcMapStats mapStats = new SvcMapStats();
	private List<SvcPlayerStats> allPlayerStats = new ArrayList<>();
	private List<SvcRoundStats> allRoundStats = new ArrayList<>();
	private List<SvcPlayerRoundStats> allPlayerRoundStats = new ArrayList<>();
	private List<SvcRoundKillEvent> allRoundKillEvents = new ArrayList<>();
	private List<SvcRoundShotEvent> allRoundShotEvents = new ArrayList<>();
	private List<SvcRoundHitEvent> allRoundHitEvents = new ArrayList<>();
	private List<SvcRoundEvent> allRoundEvents = new ArrayList<>();
}
