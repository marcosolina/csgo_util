package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ixigo.demmanager.enums.PlayerSide;
import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * DTO model of the table which contains the data extracted from the DEM files
 * 
 * @author marco
 *
 */
@FieldNameConstants
@Getter
@Setter
public class Users_scoresDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	/**
	 * The date when the map was played
	 * 
	 * @param The date when the map was played.
	 * @return The date when the map was played.
	 */
	private LocalDateTime game_date = null;
	/**
	 * Name of the map
	 * 
	 * @param The name of the map played.
	 * @return The name of the map played.
	 */
	private String map = "";
	/**
	 * The user Steam ID
	 * 
	 * @param The user Steam ID.
	 * @return The user Steam ID.
	 */
	private String steam_id = "";
	/**
	 * Number of kills made by the user
	 * 
	 * @param Number of kills made by the user.
	 * @return Number of kills made by the user.
	 */
	private Long kills = null;
	/**
	 * Team kill friendly fire
	 * 
	 * @param Number of friendly fire kills.
	 * @return Number of friendly fire kills.
	 */
	private Long ff = null;
	/**
	 * Number of bombs defused
	 * 
	 * @param Number of bombs defused.
	 * @return Number of bombs defused.
	 */
	private Long bd = null;
	/**
	 * Head shots percentage
	 * 
	 * @param Head shots percentage.
	 * @return Head shots percentage.
	 */
	private BigDecimal hsp = BigDecimal.ZERO;
	/**
	 * One versus five
	 * 
	 * @param One versus five.
	 * @return One versus five.
	 */
	private Long _1v5 = null;
	/**
	 * One versus four
	 * 
	 * @param One versus four.
	 * @return One versus four.
	 */
	private Long _1v4 = null;
	/**
	 * One versus three
	 * 
	 * @param One versus three.
	 * @return One versus three.
	 */
	private Long _1v3 = null;
	/**
	 * One versus two
	 * 
	 * @param One versus two.
	 * @return One versus two.
	 */
	private Long _1v2 = null;
	/**
	 * One versus one
	 * 
	 * @param One versus one.
	 * @return One versus one.
	 */
	private Long _1v1 = null;
	/**
	 * High explosive damage
	 * 
	 * @param High explosive damage.
	 * @return High explosive damage.
	 */
	private Long hed = null;
	/**
	 * Head shots
	 * 
	 * @param Head shots.
	 * @return Head shots.
	 */
	private Long hs = null;
	/**
	 * Flashes thrown counter
	 * 
	 * @param Flashes thrown counter.
	 * @return Flashes thrown counter.
	 */
	private Long flashes = null;
	/**
	 * Bomb planted
	 * 
	 * @param Bomb planted.
	 * @return Bomb planted.
	 */
	private Long bp = null;
	/**
	 * Round win share
	 * 
	 * @param Round win share.
	 * @return Round win share.
	 */
	private BigDecimal rws = BigDecimal.ZERO;
	/**
	 * User score
	 * 
	 * @param User score.
	 * @return User score.
	 */
	private Long score = null;
	/**
	 * Grenades thrown counter
	 * 
	 * @param Grenades thrown counter.
	 * @return Grenades thrown counter.
	 */
	private Long grenades = null;
	/**
	 * Assists counter
	 * 
	 * @param Assists counter.
	 * @return Assists counter.
	 */
	private Long assists = null;
	/**
	 * Smokes thrown counter
	 * 
	 * @param Smokes thrown counter.
	 * @return Smokes thrown counter.
	 */
	private Long smokes = null;
	/**
	 * Five kills counter
	 * 
	 * @param Five kills counter.
	 * @return Five kills counter.
	 */
	private Long _5k = null;
	/**
	 * Four kills counter
	 * 
	 * @param Four kills counter.
	 * @return Four kills counter.
	 */
	private Long _4k = null;
	/**
	 * Three kills counter
	 * 
	 * @param Three kills counter.
	 * @return Three kills counter.
	 */
	private Long _3k = null;

	/**
	 * Two kills counter
	 * 
	 * @param Two kills counter.
	 * @return Two kills counter.
	 */
	private Long _2k = null;
	/**
	 * One kill counter
	 * 
	 * @param One kill counter.
	 * @return One kill counter.
	 */
	private Long _1k = null;
	/**
	 * Fire thrown counter
	 * 
	 * @param Fire thrown counter.
	 * @return Fire thrown counter.
	 */
	private Long fire = null;
	/**
	 * Deaths
	 * 
	 * @param Deaths.
	 * @return Deaths.
	 */
	private Long deaths = null;
	/**
	 * Assists per round
	 * 
	 * @param Assists per round.
	 * @return Assists per round.
	 */
	private BigDecimal apr = BigDecimal.ZERO;
	/**
	 * Match played
	 * 
	 * @param Match played.
	 * @return Match played.
	 */
	private BigDecimal mp = BigDecimal.ZERO;
	/**
	 * DEM file name
	 * 
	 * @param DEM file name.
	 * @return DEM file name.
	 */
	private String file_name = "";
	/**
	 * Entry kill
	 * 
	 * @param Entry kill.
	 * @return Entry kill.
	 */
	private Long ek = null;
	/**
	 * Most valuable player
	 * 
	 * @param Most valuable player.
	 * @return Most valuable player.
	 */
	private Long mvp = null;
	/**
	 * Death per round
	 * 
	 * @param Death per round.
	 * @return Death per round.
	 */
	private BigDecimal dpr = BigDecimal.ZERO;
	/**
	 * Kill per round
	 * 
	 * @param Kill per round.
	 * @return Kill per round.
	 */
	private BigDecimal kpr = BigDecimal.ZERO;
	/**
	 * Average damage per round
	 * 
	 * @param Average damage per round.
	 * @return Average damage per round.
	 */
	private BigDecimal adr = BigDecimal.ZERO;
	/**
	 * Trade death
	 * 
	 * @param Trade death.
	 * @return Trade death.
	 */
	private Long td = null;
	/**
	 * Total damage armor
	 * 
	 * @param Total damage armor.
	 * @return Total damage armor.
	 */
	private Long tda = null;
	/**
	 * Half life television rating
	 * 
	 * @param Half life television rating.
	 * @return Half life television rating.
	 */
	private BigDecimal hltv = BigDecimal.ZERO;
	/**
	 * Trade kill
	 * 
	 * @param Trade kill.
	 * @return Trade kill.
	 */
	private Long tk = null;
	/**
	 * Kill death ratio
	 * 
	 * @param Kill death ratio.
	 * @return Kill death ratio.
	 */
	private BigDecimal kdr = BigDecimal.ZERO;
	/**
	 * Total damage health
	 * 
	 * @param Total damage health.
	 * @return Total damage health.
	 */
	private Long tdh = null;

	/**
	 * Fire damage
	 * 
	 * @param Fire damage.
	 * @return Fire damage.
	 */
	private Long fd = null;

	/**
	 * In which team the player has played the match
	 */
	private PlayerSide side;

}
