package com.ixigo.demmanager.models.svc.demdata.nodejs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ixigo.demmanager.models.svc.demdata.data.SvcMatchStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerRoundStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcPlayerStats;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundEvents;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundHitEvents;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundKillEvents;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundShotEvents;
import com.ixigo.demmanager.models.svc.demdata.data.SvcRoundStats;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcNodeJsParseOutput implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean isCs2DemFile;
	private SvcMatchStats mapStats = new SvcMatchStats();
	private List<SvcPlayerStats> allPlayerStats = new ArrayList<>();
	private List<SvcRoundStats> allRoundStats = new ArrayList<>();
	private List<SvcPlayerRoundStats> allPlayerRoundStats = new ArrayList<>();
	private List<SvcRoundKillEvents> allRoundKillEvents = new ArrayList<>();
	private List<SvcRoundShotEvents> allRoundShotEvents = new ArrayList<>();
	private List<SvcRoundHitEvents> allRoundHitEvents = new ArrayList<>();
	private List<SvcRoundEvents> allRoundEvents = new ArrayList<>();
}
