package com.ixigo.demmanager.models.database;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

/**
 * DAO representing the DB table which contains the info extracted from the DEM
 * files
 * 
 * @author marco
 *
 */
public class Users_scoresDao extends IxigoDao<Users_scoresDto> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(Users_scoresDao.class);

	private static final long serialVersionUID = 1L;
	public static final String tableName = "users_scores";
	private Users_scoresDto dto = null;

	public Users_scoresDao() {
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {
			Users_scoresDto.Fields.map,
			Users_scoresDto.Fields.steam_id,
			Users_scoresDto.Fields.game_date
		});
		this.setSqlFields(new String[] {
			Users_scoresDto.Fields.kills,
			Users_scoresDto.Fields.ff,
			Users_scoresDto.Fields.bd,
			Users_scoresDto.Fields.hsp,
			Users_scoresDto.Fields._1v3,
			Users_scoresDto.Fields._1v2,
			Users_scoresDto.Fields._1v1,
			Users_scoresDto.Fields.hed,
			Users_scoresDto.Fields.hs,
			Users_scoresDto.Fields.flashes,
			Users_scoresDto.Fields.bp,
			Users_scoresDto.Fields.rws,
			Users_scoresDto.Fields.score,
			Users_scoresDto.Fields.grenades,
			Users_scoresDto.Fields.assists,
			Users_scoresDto.Fields.smokes,
			Users_scoresDto.Fields._4k,
			Users_scoresDto.Fields._2k,
			Users_scoresDto.Fields.fire,
			Users_scoresDto.Fields._1v5,
			Users_scoresDto.Fields._1v4,
			Users_scoresDto.Fields.map,
			Users_scoresDto.Fields.deaths,
			Users_scoresDto.Fields.apr,
			Users_scoresDto.Fields.mp,
			Users_scoresDto.Fields.file_name,
			Users_scoresDto.Fields.ek,
			Users_scoresDto.Fields.mvp,
			Users_scoresDto.Fields.dpr,
			Users_scoresDto.Fields.kpr,
			Users_scoresDto.Fields.adr,
			Users_scoresDto.Fields.td,
			Users_scoresDto.Fields.tda,
			Users_scoresDto.Fields._5k,
			Users_scoresDto.Fields.hltv,
			Users_scoresDto.Fields._3k,
			Users_scoresDto.Fields.steam_id,
			Users_scoresDto.Fields.tk,
			Users_scoresDto.Fields.kdr,
			Users_scoresDto.Fields._1k,
			Users_scoresDto.Fields.tdh,
			Users_scoresDto.Fields.game_date,
			Users_scoresDto.Fields.fd
		});
		// @formatter:on
		this.dto = new Users_scoresDto();
	}

	@Override
	public Users_scoresDto mappingFunction(Row row, RowMetadata rowMetaData) {
		Users_scoresDto dto = new Users_scoresDto();

		Arrays.asList(this.getSqlFields()).forEach(field -> {
			try {
				Method[] m = dto.getClass().getMethods();
				for (int j = 0; j < m.length; j++) {
					String setterName = "set" + StringUtils.capitalize(field);
					if (m[j].getName().equals(setterName) && m[j].getParameterTypes().length == 1) {
						Object value = row.get(field, m[j].getParameterTypes()[0]);
						m[j].invoke(dto, new Object[] { value });
					}
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
				_LOGGER.error(e.getMessage());
				if (_LOGGER.isTraceEnabled()) {
					e.printStackTrace();
				}
			}
		});

		// dto.setFile_name(row.get(Dem_process_queueDto.Fields.file_name,
		// String.class));

		return dto;
	}

	public Long getKills() {
		return dto.getKills();
	}

	public void setKills(Long kills) {
		this.dto.setKills(kills);
	}

	public Long getFf() {
		return dto.getFf();
	}

	public void setFf(Long ff) {
		this.dto.setFf(ff);
	}

	public Long getBd() {
		return dto.getBd();
	}

	public void setBd(Long bd) {
		this.dto.setBd(bd);
	}

	public BigDecimal getHsp() {
		return dto.getHsp();
	}

	public void setHsp(BigDecimal hsp) {
		this.dto.setHsp(hsp);
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

	public Long get_1v1() {
		return dto.get_1v1();
	}

	public void set_1v1(Long _1v1) {
		this.dto.set_1v1(_1v1);
	}

	public Long getHed() {
		return dto.getHed();
	}

	public void setHed(Long hed) {
		this.dto.setHed(hed);
	}

	public Long getHs() {
		return dto.getHs();
	}

	public void setHs(Long hs) {
		this.dto.setHs(hs);
	}

	public Long getFlashes() {
		return dto.getFlashes();
	}

	public void setFlashes(Long flashes) {
		this.dto.setFlashes(flashes);
	}

	public Long getBp() {
		return dto.getBp();
	}

	public void setBp(Long bp) {
		this.dto.setBp(bp);
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

	public Long getGrenades() {
		return dto.getGrenades();
	}

	public void setGrenades(Long grenades) {
		this.dto.setGrenades(grenades);
	}

	public Long getAssists() {
		return dto.getAssists();
	}

	public void setAssists(Long assists) {
		this.dto.setAssists(assists);
	}

	public Long getSmokes() {
		return dto.getSmokes();
	}

	public void setSmokes(Long smokes) {
		this.dto.setSmokes(smokes);
	}

	public Long get_4k() {
		return dto.get_4k();
	}

	public void set_4k(Long _4k) {
		this.dto.set_4k(_4k);
	}

	public Long get_2k() {
		return dto.get_2k();
	}

	public void set_2k(Long _2k) {
		this.dto.set_2k(_2k);
	}

	public Long getFire() {
		return dto.getFire();
	}

	public void setFire(Long fire) {
		this.dto.setFire(fire);
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

	public String getMap() {
		return dto.getMap();
	}

	public void setMap(String map) {
		this.dto.setMap(map);
	}

	public Long getDeaths() {
		return dto.getDeaths();
	}

	public void setDeaths(Long deaths) {
		this.dto.setDeaths(deaths);
	}

	public BigDecimal getApr() {
		return dto.getApr();
	}

	public void setApr(BigDecimal apr) {
		this.dto.setApr(apr);
	}

	public BigDecimal getMp() {
		return dto.getMp();
	}

	public void setMp(BigDecimal mp) {
		this.dto.setMp(mp);
	}

	public String getFile_name() {
		return dto.getFile_name();
	}

	public void setFile_name(String file_name) {
		this.dto.setFile_name(file_name);
	}

	public Long getEk() {
		return dto.getEk();
	}

	public void setEk(Long ek) {
		this.dto.setEk(ek);
	}

	public Long getMvp() {
		return dto.getMvp();
	}

	public void setMvp(Long mvp) {
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

	public BigDecimal getAdr() {
		return dto.getAdr();
	}

	public void setAdr(BigDecimal adr) {
		this.dto.setAdr(adr);
	}

	public Long getTd() {
		return dto.getTd();
	}

	public void setTd(Long td) {
		this.dto.setTd(td);
	}

	public Long getTda() {
		return dto.getTda();
	}

	public void setTda(Long tda) {
		this.dto.setTda(tda);
	}

	public Long get_5k() {
		return dto.get_5k();
	}

	public void set_5k(Long _5k) {
		this.dto.set_5k(_5k);
	}

	public BigDecimal getHltv() {
		return dto.getHltv();
	}

	public void setHltv(BigDecimal hltv) {
		this.dto.setHltv(hltv);
	}

	public Long get_3k() {
		return dto.get_3k();
	}

	public void set_3k(Long _3k) {
		this.dto.set_3k(_3k);
	}

	public String getSteam_id() {
		return dto.getSteam_id();
	}

	public void setSteam_id(String steam_id) {
		this.dto.setSteam_id(steam_id);
	}

	public Long getTk() {
		return dto.getTk();
	}

	public void setTk(Long tk) {
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

	public Long getTdh() {
		return dto.getTdh();
	}

	public void setTdh(Long tdh) {
		this.dto.setTdh(tdh);
	}

	public LocalDateTime getGame_date() {
		return dto.getGame_date();
	}

	public void setGame_date(LocalDateTime game_date) {
		this.dto.setGame_date(game_date);
	}

	public Long getFd() {
		return dto.getFd();
	}

	public void setFd(Long fd) {
		this.dto.setFd(fd);
	}

	public Users_scoresDto getDto() {
		return this.dto;
	}

	public void setDto(Users_scoresDto dto) {
		this.dto = dto;
	}
}
