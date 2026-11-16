package com.ixigo.discordbot.models.repo;

import com.ixigo.library.dto.IxigoDto;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public class Users_mapDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steam_id = null;
	private String steam_name = null;
	private String discord_name = null;
	private Long discord_id = null;

	public String getSteam_id() {
		return this.steam_id;
	}

	public void setSteam_id(String steam_id) {
		this.steam_id = steam_id;
	}

	public String getSteam_name() {
		return this.steam_name;
	}

	public void setSteam_name(String steam_name) {
		this.steam_name = steam_name;
	}

	public String getDiscord_name() {
		return this.discord_name;
	}

	public void setDiscord_name(String discord_name) {
		this.discord_name = discord_name;
	}

	public Long getDiscord_id() {
		return this.discord_id;
	}

	public void setDiscord_id(Long discord_id) {
		this.discord_id = discord_id;
	}

}
