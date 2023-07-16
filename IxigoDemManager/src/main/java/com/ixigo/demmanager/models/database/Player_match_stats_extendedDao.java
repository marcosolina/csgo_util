package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_match_stats_extendedDao extends IxigoDao<Player_match_stats_extendedDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_match_stats_extendedDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_match_stats_extended";
	private Player_match_stats_extendedDto dto = null;

	public Player_match_stats_extendedDao() {
		_LOGGER.trace("Instanciating Player_match_stats_extendedDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
			Player_match_stats_extendedDto.Fields.kills,
			Player_match_stats_extendedDto.Fields.ff,
			Player_match_stats_extendedDto.Fields.hltv_rating,
			Player_match_stats_extendedDto.Fields.bd,
			Player_match_stats_extendedDto.Fields._1v3,
			Player_match_stats_extendedDto.Fields._1v2,
			Player_match_stats_extendedDto.Fields.kast,
			Player_match_stats_extendedDto.Fields._1v1,
			Player_match_stats_extendedDto.Fields.hr,
			Player_match_stats_extendedDto.Fields.match_date,
			Player_match_stats_extendedDto.Fields.bp,
			Player_match_stats_extendedDto.Fields.ud,
			Player_match_stats_extendedDto.Fields.kasttotal,
			Player_match_stats_extendedDto.Fields.rws,
			Player_match_stats_extendedDto.Fields.score,
			Player_match_stats_extendedDto.Fields.headshots,
			Player_match_stats_extendedDto.Fields.assists,
			Player_match_stats_extendedDto.Fields._4k,
			Player_match_stats_extendedDto.Fields.last_round_team,
			Player_match_stats_extendedDto.Fields._2k,
			Player_match_stats_extendedDto.Fields.usernames,
			Player_match_stats_extendedDto.Fields._1v5,
			Player_match_stats_extendedDto.Fields._1v4,
			Player_match_stats_extendedDto.Fields.headshot_percentage,
			Player_match_stats_extendedDto.Fields.deaths,
			Player_match_stats_extendedDto.Fields.rounds_on_team2,
			Player_match_stats_extendedDto.Fields.rounds_on_team1,
			Player_match_stats_extendedDto.Fields.ffd,
			Player_match_stats_extendedDto.Fields.roundsplayed,
			Player_match_stats_extendedDto.Fields.ek,
			Player_match_stats_extendedDto.Fields.mvp,
			Player_match_stats_extendedDto.Fields.match_id,
			Player_match_stats_extendedDto.Fields.dpr,
			Player_match_stats_extendedDto.Fields.rwstotal,
			Player_match_stats_extendedDto.Fields.kpr,
			Player_match_stats_extendedDto.Fields.adr,
			Player_match_stats_extendedDto.Fields.steamid,
			Player_match_stats_extendedDto.Fields.td,
			Player_match_stats_extendedDto.Fields.tda,
			Player_match_stats_extendedDto.Fields._5k,
			Player_match_stats_extendedDto.Fields._3k,
			Player_match_stats_extendedDto.Fields.ebt,
			Player_match_stats_extendedDto.Fields.tk,
			Player_match_stats_extendedDto.Fields.kdr,
			Player_match_stats_extendedDto.Fields._1k,
			Player_match_stats_extendedDto.Fields.tdh,
			Player_match_stats_extendedDto.Fields.fbt,
			Player_match_stats_extendedDto.Fields.fa,
		});
		// @formatter:on
		this.dto = new Player_match_stats_extendedDto();
	}

	@Override
	public Player_match_stats_extendedDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_match_stats_extendedDto(), row, rowMetaData);
	}

	public BigDecimal getKills() {
		return dto.getKills();
	}

	public void setKills(BigDecimal kills) {
		this.dto.setKills(kills);
	}

	public BigDecimal getFf() {
		return dto.getFf();
	}

	public void setFf(BigDecimal ff) {
		this.dto.setFf(ff);
	}

	public BigDecimal getHltv_rating() {
		return dto.getHltv_rating();
	}

	public void setHltv_rating(BigDecimal hltv_rating) {
		this.dto.setHltv_rating(hltv_rating);
	}

	public BigDecimal getBd() {
		return dto.getBd();
	}

	public void setBd(BigDecimal bd) {
		this.dto.setBd(bd);
	}

	public Long get_1v3() {
		return dto.get_1v3();
	}

	public void set_1v3(Long _1v3) {
		this.dto.set_1v3(_1v3);
	}

	public Long get_1v2() {
		return dto.get_1v2();
	}

	public void set_1v2(Long _1v2) {
		this.dto.set_1v2(_1v2);
	}

	public BigDecimal getKast() {
		return dto.getKast();
	}

	public void setKast(BigDecimal kast) {
		this.dto.setKast(kast);
	}

	public Long get_1v1() {
		return dto.get_1v1();
	}

	public void set_1v1(Long _1v1) {
		this.dto.set_1v1(_1v1);
	}

	public BigDecimal getHr() {
		return dto.getHr();
	}

	public void setHr(BigDecimal hr) {
		this.dto.setHr(hr);
	}

	public LocalDateTime getMatch_date() {
		return dto.getMatch_date();
	}

	public void setMatch_date(LocalDateTime match_date) {
		this.dto.setMatch_date(match_date);
	}

	public BigDecimal getBp() {
		return dto.getBp();
	}

	public void setBp(BigDecimal bp) {
		this.dto.setBp(bp);
	}

	public BigDecimal getUd() {
		return dto.getUd();
	}

	public void setUd(BigDecimal ud) {
		this.dto.setUd(ud);
	}

	public Long getKasttotal() {
		return dto.getKasttotal();
	}

	public void setKasttotal(Long kasttotal) {
		this.dto.setKasttotal(kasttotal);
	}

	public BigDecimal getRws() {
		return dto.getRws();
	}

	public void setRws(BigDecimal rws) {
		this.dto.setRws(rws);
	}

	public Long getScore() {
		return dto.getScore();
	}

	public void setScore(Long score) {
		this.dto.setScore(score);
	}

	public BigDecimal getHeadshots() {
		return dto.getHeadshots();
	}

	public void setHeadshots(BigDecimal headshots) {
		this.dto.setHeadshots(headshots);
	}

	public BigDecimal getAssists() {
		return dto.getAssists();
	}

	public void setAssists(BigDecimal assists) {
		this.dto.setAssists(assists);
	}

	public Long get_4k() {
		return dto.get_4k();
	}

	public void set_4k(Long _4k) {
		this.dto.set_4k(_4k);
	}

	public String getLast_round_team() {
		return dto.getLast_round_team();
	}

	public void setLast_round_team(String last_round_team) {
		this.dto.setLast_round_team(last_round_team);
	}

	public Long get_2k() {
		return dto.get_2k();
	}

	public void set_2k(Long _2k) {
		this.dto.set_2k(_2k);
	}

	public String getUsernames() {
		return dto.getUsernames();
	}

	public void setUsernames(String usernames) {
		this.dto.setUsernames(usernames);
	}

	public Long get_1v5() {
		return dto.get_1v5();
	}

	public void set_1v5(Long _1v5) {
		this.dto.set_1v5(_1v5);
	}

	public Long get_1v4() {
		return dto.get_1v4();
	}

	public void set_1v4(Long _1v4) {
		this.dto.set_1v4(_1v4);
	}

	public BigDecimal getHeadshot_percentage() {
		return dto.getHeadshot_percentage();
	}

	public void setHeadshot_percentage(BigDecimal headshot_percentage) {
		this.dto.setHeadshot_percentage(headshot_percentage);
	}

	public BigDecimal getDeaths() {
		return dto.getDeaths();
	}

	public void setDeaths(BigDecimal deaths) {
		this.dto.setDeaths(deaths);
	}

	public Long getRounds_on_team2() {
		return dto.getRounds_on_team2();
	}

	public void setRounds_on_team2(Long rounds_on_team2) {
		this.dto.setRounds_on_team2(rounds_on_team2);
	}

	public Long getRounds_on_team1() {
		return dto.getRounds_on_team1();
	}

	public void setRounds_on_team1(Long rounds_on_team1) {
		this.dto.setRounds_on_team1(rounds_on_team1);
	}

	public BigDecimal getFfd() {
		return dto.getFfd();
	}

	public void setFfd(BigDecimal ffd) {
		this.dto.setFfd(ffd);
	}

	public Long getRoundsplayed() {
		return dto.getRoundsplayed();
	}

	public void setRoundsplayed(Long roundsplayed) {
		this.dto.setRoundsplayed(roundsplayed);
	}

	public BigDecimal getEk() {
		return dto.getEk();
	}

	public void setEk(BigDecimal ek) {
		this.dto.setEk(ek);
	}

	public Long getMvp() {
		return dto.getMvp();
	}

	public void setMvp(Long mvp) {
		this.dto.setMvp(mvp);
	}

	public Long getMatch_id() {
		return dto.getMatch_id();
	}

	public void setMatch_id(Long match_id) {
		this.dto.setMatch_id(match_id);
	}

	public BigDecimal getDpr() {
		return dto.getDpr();
	}

	public void setDpr(BigDecimal dpr) {
		this.dto.setDpr(dpr);
	}

	public BigDecimal getRwstotal() {
		return dto.getRwstotal();
	}

	public void setRwstotal(BigDecimal rwstotal) {
		this.dto.setRwstotal(rwstotal);
	}

	public BigDecimal getKpr() {
		return dto.getKpr();
	}

	public void setKpr(BigDecimal kpr) {
		this.dto.setKpr(kpr);
	}

	public BigDecimal getAdr() {
		return dto.getAdr();
	}

	public void setAdr(BigDecimal adr) {
		this.dto.setAdr(adr);
	}

	public String getSteamid() {
		return dto.getSteamid();
	}

	public void setSteamid(String steamid) {
		this.dto.setSteamid(steamid);
	}

	public BigDecimal getTd() {
		return dto.getTd();
	}

	public void setTd(BigDecimal td) {
		this.dto.setTd(td);
	}

	public BigDecimal getTda() {
		return dto.getTda();
	}

	public void setTda(BigDecimal tda) {
		this.dto.setTda(tda);
	}

	public Long get_5k() {
		return dto.get_5k();
	}

	public void set_5k(Long _5k) {
		this.dto.set_5k(_5k);
	}

	public Long get_3k() {
		return dto.get_3k();
	}

	public void set_3k(Long _3k) {
		this.dto.set_3k(_3k);
	}

	public BigDecimal getEbt() {
		return dto.getEbt();
	}

	public void setEbt(BigDecimal ebt) {
		this.dto.setEbt(ebt);
	}

	public BigDecimal getTk() {
		return dto.getTk();
	}

	public void setTk(BigDecimal tk) {
		this.dto.setTk(tk);
	}

	public BigDecimal getKdr() {
		return dto.getKdr();
	}

	public void setKdr(BigDecimal kdr) {
		this.dto.setKdr(kdr);
	}

	public Long get_1k() {
		return dto.get_1k();
	}

	public void set_1k(Long _1k) {
		this.dto.set_1k(_1k);
	}

	public BigDecimal getTdh() {
		return dto.getTdh();
	}

	public void setTdh(BigDecimal tdh) {
		this.dto.setTdh(tdh);
	}

	public BigDecimal getFbt() {
		return dto.getFbt();
	}

	public void setFbt(BigDecimal fbt) {
		this.dto.setFbt(fbt);
	}

	public BigDecimal getFa() {
		return dto.getFa();
	}

	public void setFa(BigDecimal fa) {
		this.dto.setFa(fa);
	}

	public Player_match_stats_extendedDto getDto() {
		return this.dto;
	}

	public void setDto(Player_match_stats_extendedDto dto) {
		this.dto = dto;
	}

}
