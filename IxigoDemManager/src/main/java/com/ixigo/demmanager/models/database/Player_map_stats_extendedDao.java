package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Player_map_stats_extendedDao extends IxigoDao<Player_map_stats_extendedDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(Player_map_stats_extendedDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "player_map_stats_extended";
	private Player_map_stats_extendedDto dto = null;

	public Player_map_stats_extendedDao() {
		_LOGGER.trace("Instanciating Player_map_stats_extendedDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlFields(new String[] {
			Player_map_stats_extendedDto.Fields.kills,
			Player_map_stats_extendedDto.Fields.ff,
			Player_map_stats_extendedDto.Fields.hltv_rating,
			Player_map_stats_extendedDto.Fields.bd,
			Player_map_stats_extendedDto.Fields._1v3,
			Player_map_stats_extendedDto.Fields._1v2,
			Player_map_stats_extendedDto.Fields.kast,
			Player_map_stats_extendedDto.Fields._1v1,
			Player_map_stats_extendedDto.Fields.hr,
			Player_map_stats_extendedDto.Fields.mapname,
			Player_map_stats_extendedDto.Fields.bp,
			Player_map_stats_extendedDto.Fields.ud,
			Player_map_stats_extendedDto.Fields.rws,
			Player_map_stats_extendedDto.Fields.headshots,
			Player_map_stats_extendedDto.Fields.assists,
			Player_map_stats_extendedDto.Fields._4k,
			Player_map_stats_extendedDto.Fields._2k,
			Player_map_stats_extendedDto.Fields.usernames,
			Player_map_stats_extendedDto.Fields._1v5,
			Player_map_stats_extendedDto.Fields._1v4,
			Player_map_stats_extendedDto.Fields.headshot_percentage,
			Player_map_stats_extendedDto.Fields.deaths,
			Player_map_stats_extendedDto.Fields.ffd,
			Player_map_stats_extendedDto.Fields.ek,
			Player_map_stats_extendedDto.Fields.mvp,
			Player_map_stats_extendedDto.Fields.dpr,
			Player_map_stats_extendedDto.Fields.kpr,
			Player_map_stats_extendedDto.Fields.matches,
			Player_map_stats_extendedDto.Fields.adr,
			Player_map_stats_extendedDto.Fields.steamid,
			Player_map_stats_extendedDto.Fields.td,
			Player_map_stats_extendedDto.Fields.tda,
			Player_map_stats_extendedDto.Fields._5k,
			Player_map_stats_extendedDto.Fields._3k,
			Player_map_stats_extendedDto.Fields.ebt,
			Player_map_stats_extendedDto.Fields.tk,
			Player_map_stats_extendedDto.Fields.kdr,
			Player_map_stats_extendedDto.Fields._1k,
			Player_map_stats_extendedDto.Fields.tdh,
			Player_map_stats_extendedDto.Fields.fbt,
			Player_map_stats_extendedDto.Fields.fa,
			Player_map_stats_extendedDto.Fields.rounds,
		});
		// @formatter:on
		this.dto = new Player_map_stats_extendedDto();
	}

	@Override
	public Player_map_stats_extendedDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new Player_map_stats_extendedDto(), row, rowMetaData);
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

	public BigDecimal get_1v3() {
		return dto.get_1v3();
	}

	public void set_1v3(BigDecimal _1v3) {
		this.dto.set_1v3(_1v3);
	}

	public BigDecimal get_1v2() {
		return dto.get_1v2();
	}

	public void set_1v2(BigDecimal _1v2) {
		this.dto.set_1v2(_1v2);
	}

	public BigDecimal getKast() {
		return dto.getKast();
	}

	public void setKast(BigDecimal kast) {
		this.dto.setKast(kast);
	}

	public BigDecimal get_1v1() {
		return dto.get_1v1();
	}

	public void set_1v1(BigDecimal _1v1) {
		this.dto.set_1v1(_1v1);
	}

	public BigDecimal getHr() {
		return dto.getHr();
	}

	public void setHr(BigDecimal hr) {
		this.dto.setHr(hr);
	}

	public String getMapname() {
		return dto.getMapname();
	}

	public void setMapname(String mapname) {
		this.dto.setMapname(mapname);
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

	public BigDecimal getRws() {
		return dto.getRws();
	}

	public void setRws(BigDecimal rws) {
		this.dto.setRws(rws);
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

	public BigDecimal get_4k() {
		return dto.get_4k();
	}

	public void set_4k(BigDecimal _4k) {
		this.dto.set_4k(_4k);
	}

	public BigDecimal get_2k() {
		return dto.get_2k();
	}

	public void set_2k(BigDecimal _2k) {
		this.dto.set_2k(_2k);
	}

	public String getUsernames() {
		return dto.getUsernames();
	}

	public void setUsernames(String usernames) {
		this.dto.setUsernames(usernames);
	}

	public BigDecimal get_1v5() {
		return dto.get_1v5();
	}

	public void set_1v5(BigDecimal _1v5) {
		this.dto.set_1v5(_1v5);
	}

	public BigDecimal get_1v4() {
		return dto.get_1v4();
	}

	public void set_1v4(BigDecimal _1v4) {
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

	public BigDecimal getFfd() {
		return dto.getFfd();
	}

	public void setFfd(BigDecimal ffd) {
		this.dto.setFfd(ffd);
	}

	public BigDecimal getEk() {
		return dto.getEk();
	}

	public void setEk(BigDecimal ek) {
		this.dto.setEk(ek);
	}

	public BigDecimal getMvp() {
		return dto.getMvp();
	}

	public void setMvp(BigDecimal mvp) {
		this.dto.setMvp(mvp);
	}

	public BigDecimal getDpr() {
		return dto.getDpr();
	}

	public void setDpr(BigDecimal dpr) {
		this.dto.setDpr(dpr);
	}

	public BigDecimal getKpr() {
		return dto.getKpr();
	}

	public void setKpr(BigDecimal kpr) {
		this.dto.setKpr(kpr);
	}

	public Long getMatches() {
		return dto.getMatches();
	}

	public void setMatches(Long matches) {
		this.dto.setMatches(matches);
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

	public BigDecimal get_5k() {
		return dto.get_5k();
	}

	public void set_5k(BigDecimal _5k) {
		this.dto.set_5k(_5k);
	}

	public BigDecimal get_3k() {
		return dto.get_3k();
	}

	public void set_3k(BigDecimal _3k) {
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

	public BigDecimal get_1k() {
		return dto.get_1k();
	}

	public void set_1k(BigDecimal _1k) {
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

	public BigDecimal getRounds() {
		return dto.getRounds();
	}

	public void setRounds(BigDecimal rounds) {
		this.dto.setRounds(rounds);
	}

	public Player_map_stats_extendedDto getDto() {
		return this.dto;
	}

	public void setDto(Player_map_stats_extendedDto dto) {
		this.dto = dto;
	}

}
