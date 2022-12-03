package com.ixigo.discordbot.models.repo;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Users_mapDao extends IxigoDao<Users_mapDto> {

	private static final long serialVersionUID = 1L;
	private Users_mapDto dto = null;

	public Users_mapDao() {
		this.setSqlViewName("users_map");
		this.setSqlKeys(new String[] { Users_mapDto.Fields.discord_id });
		// @formatter:off
		this.setSqlFields(new String[] {
			Users_mapDto.Fields.steam_id,
			Users_mapDto.Fields.steam_name,
			Users_mapDto.Fields.discord_name,
			Users_mapDto.Fields.discord_id
		});
		// @formatter:on

		this.dto = new Users_mapDto();
	}

	@Override
	public Users_mapDto mappingFunction(Row row, RowMetadata rowMetaData) {
		Users_mapDto dto = new Users_mapDto();
		dto.setSteam_id(row.get(Users_mapDto.Fields.steam_id, String.class));
		dto.setDiscord_id(row.get(Users_mapDto.Fields.discord_id, Long.class));
		dto.setSteam_name(row.get(Users_mapDto.Fields.steam_name, String.class));
		dto.setDiscord_name(row.get(Users_mapDto.Fields.discord_name, String.class));
		return dto;
	}

	public String getSteam_id() {
		return dto.getSteam_id();
	}

	public void setSteam_id(String steam_id) {
		this.dto.setSteam_id(steam_id);
	}

	public String getSteam_name() {
		return dto.getSteam_name();
	}

	public void setSteam_name(String steam_name) {
		this.dto.setSteam_name(steam_name);
	}

	public String getDiscord_name() {
		return dto.getDiscord_name();
	}

	public void setDiscord_name(String discord_name) {
		this.dto.setDiscord_name(discord_name);
	}

	public Long getDiscord_id() {
		return dto.getDiscord_id();
	}

	public void setDiscord_id(Long discord_id) {
		this.dto.setDiscord_id(discord_id);
	}

	public Users_mapDto getDto() {
		return this.dto;
	}

	public void setDto(Users_mapDto dto) {
		this.dto = dto;
	}

}
