package com.marco.csgoutil.roundparser.model.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EntityUserScorePk implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "GAME_DATE")
	private LocalDateTime gameDate;
	@Column(name = "MAP")
	private String map;
	@Column(name = "STEAM_ID")
	private String steamId;

	public LocalDateTime getGameDate() {
		return gameDate;
	}

	public void setGameDate(LocalDateTime gameDate) {
		this.gameDate = gameDate;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public String getSteamId() {
		return steamId;
	}

	public void setSteamId(String steamId) {
		this.steamId = steamId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gameDate == null) ? 0 : gameDate.hashCode());
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		result = prime * result + ((steamId == null) ? 0 : steamId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityUserScorePk other = (EntityUserScorePk) obj;
		if (gameDate == null) {
			if (other.gameDate != null)
				return false;
		} else if (!gameDate.equals(other.gameDate))
			return false;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		if (steamId == null) {
			if (other.steamId != null)
				return false;
		} else if (!steamId.equals(other.steamId))
			return false;
		return true;
	}
}
