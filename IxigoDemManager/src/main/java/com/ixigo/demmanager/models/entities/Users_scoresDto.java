package com.ixigo.demmanager.models.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ixigo.library.dto.IxigoDto;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public class Users_scoresDto implements IxigoDto{

	private static final long serialVersionUID = 1L;
	private Long kills = null;
	private Long ff = null;
	private Long bd = null;
	private BigDecimal hsp = BigDecimal.ZERO;
	private Long _1v3 = null;
	private Long _1v2 = null;
	private Long _1v1 = null;
	private Long hed = null;
	private Long hs = null;
	private Long flashes = null;
	private Long bp = null;
	private BigDecimal rws = BigDecimal.ZERO;
	private Long score = null;
	private Long grenades = null;
	private Long assists = null;
	private Long smokes = null;
	private Long _4k = null;
	private Long _2k = null;
	private Long fire = null;
	private Long _1v5 = null;
	private Long _1v4 = null;
	private String map = "";
	private Long deaths = null;
	private BigDecimal apr = BigDecimal.ZERO;
	private BigDecimal mp = BigDecimal.ZERO;
	private String file_name = "";
	private Long ek = null;
	private Long mvp = null;
	private BigDecimal dpr = BigDecimal.ZERO;
	private BigDecimal kpr = BigDecimal.ZERO;
	private BigDecimal adr = BigDecimal.ZERO;
	private Long td = null;
	private Long tda = null;
	private Long _5k = null;
	private BigDecimal hltv = BigDecimal.ZERO;
	private Long _3k = null;
	private String steam_id = "";
	private Long tk = null;
	private BigDecimal kdr = BigDecimal.ZERO;
	private Long _1k = null;
	private Long tdh = null;
	private LocalDateTime game_date = null;
	private Long fd = null;

	public Long getKills(){
		return this.kills;
	}

	public void setKills(Long kills){
		this.kills = kills;
	}


	public Long getFf(){
		return this.ff;
	}

	public void setFf(Long ff){
		this.ff = ff;
	}


	public Long getBd(){
		return this.bd;
	}

	public void setBd(Long bd){
		this.bd = bd;
	}


	public BigDecimal getHsp(){
		return this.hsp;
	}

	public void setHsp(BigDecimal hsp){
		this.hsp = hsp;
	}


	public Long get_1v3(){
		return this._1v3;
	}

	public void set_1v3(Long _1v3){
		this._1v3 = _1v3;
	}


	public Long get_1v2(){
		return this._1v2;
	}

	public void set_1v2(Long _1v2){
		this._1v2 = _1v2;
	}


	public Long get_1v1(){
		return this._1v1;
	}

	public void set_1v1(Long _1v1){
		this._1v1 = _1v1;
	}


	public Long getHed(){
		return this.hed;
	}

	public void setHed(Long hed){
		this.hed = hed;
	}


	public Long getHs(){
		return this.hs;
	}

	public void setHs(Long hs){
		this.hs = hs;
	}


	public Long getFlashes(){
		return this.flashes;
	}

	public void setFlashes(Long flashes){
		this.flashes = flashes;
	}


	public Long getBp(){
		return this.bp;
	}

	public void setBp(Long bp){
		this.bp = bp;
	}


	public BigDecimal getRws(){
		return this.rws;
	}

	public void setRws(BigDecimal rws){
		this.rws = rws;
	}


	public Long getScore(){
		return this.score;
	}

	public void setScore(Long score){
		this.score = score;
	}


	public Long getGrenades(){
		return this.grenades;
	}

	public void setGrenades(Long grenades){
		this.grenades = grenades;
	}


	public Long getAssists(){
		return this.assists;
	}

	public void setAssists(Long assists){
		this.assists = assists;
	}


	public Long getSmokes(){
		return this.smokes;
	}

	public void setSmokes(Long smokes){
		this.smokes = smokes;
	}


	public Long get_4k(){
		return this._4k;
	}

	public void set_4k(Long _4k){
		this._4k = _4k;
	}


	public Long get_2k(){
		return this._2k;
	}

	public void set_2k(Long _2k){
		this._2k = _2k;
	}


	public Long getFire(){
		return this.fire;
	}

	public void setFire(Long fire){
		this.fire = fire;
	}


	public Long get_1v5(){
		return this._1v5;
	}

	public void set_1v5(Long _1v5){
		this._1v5 = _1v5;
	}


	public Long get_1v4(){
		return this._1v4;
	}

	public void set_1v4(Long _1v4){
		this._1v4 = _1v4;
	}


	public String getMap(){
		return this.map;
	}

	public void setMap(String map){
		this.map = map;
	}


	public Long getDeaths(){
		return this.deaths;
	}

	public void setDeaths(Long deaths){
		this.deaths = deaths;
	}


	public BigDecimal getApr(){
		return this.apr;
	}

	public void setApr(BigDecimal apr){
		this.apr = apr;
	}


	public BigDecimal getMp(){
		return this.mp;
	}

	public void setMp(BigDecimal mp){
		this.mp = mp;
	}


	public String getFile_name(){
		return this.file_name;
	}

	public void setFile_name(String file_name){
		this.file_name = file_name;
	}


	public Long getEk(){
		return this.ek;
	}

	public void setEk(Long ek){
		this.ek = ek;
	}


	public Long getMvp(){
		return this.mvp;
	}

	public void setMvp(Long mvp){
		this.mvp = mvp;
	}


	public BigDecimal getDpr(){
		return this.dpr;
	}

	public void setDpr(BigDecimal dpr){
		this.dpr = dpr;
	}


	public BigDecimal getKpr(){
		return this.kpr;
	}

	public void setKpr(BigDecimal kpr){
		this.kpr = kpr;
	}


	public BigDecimal getAdr(){
		return this.adr;
	}

	public void setAdr(BigDecimal adr){
		this.adr = adr;
	}


	public Long getTd(){
		return this.td;
	}

	public void setTd(Long td){
		this.td = td;
	}


	public Long getTda(){
		return this.tda;
	}

	public void setTda(Long tda){
		this.tda = tda;
	}


	public Long get_5k(){
		return this._5k;
	}

	public void set_5k(Long _5k){
		this._5k = _5k;
	}


	public BigDecimal getHltv(){
		return this.hltv;
	}

	public void setHltv(BigDecimal hltv){
		this.hltv = hltv;
	}


	public Long get_3k(){
		return this._3k;
	}

	public void set_3k(Long _3k){
		this._3k = _3k;
	}


	public String getSteam_id(){
		return this.steam_id;
	}

	public void setSteam_id(String steam_id){
		this.steam_id = steam_id;
	}


	public Long getTk(){
		return this.tk;
	}

	public void setTk(Long tk){
		this.tk = tk;
	}


	public BigDecimal getKdr(){
		return this.kdr;
	}

	public void setKdr(BigDecimal kdr){
		this.kdr = kdr;
	}


	public Long get_1k(){
		return this._1k;
	}

	public void set_1k(Long _1k){
		this._1k = _1k;
	}


	public Long getTdh(){
		return this.tdh;
	}

	public void setTdh(Long tdh){
		this.tdh = tdh;
	}


	public LocalDateTime getGame_date(){
		return this.game_date;
	}

	public void setGame_date(LocalDateTime game_date){
		this.game_date = game_date;
	}


	public Long getFd(){
		return this.fd;
	}

	public void setFd(Long fd){
		this.fd = fd;
	}


}
