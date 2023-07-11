package com.ixigo.demmanager.models.svc.demdata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcWeaponStats {
	private String userName;
    private String steamID;
    private String weapon;
    private int totalKills;
    private int totalDamage;
    private double killsPerRound;
    private double damagePerRound;
    private int shotsFired;
    private double damagePerShot;
    private int hits;
    private double danagePerHit;
    private double accuracyPerc;
    private double headshotPerc;
    private double chestPerc;
    private double stomachPerc;
    private double armsPerc;
    private double legsPerc;
}
