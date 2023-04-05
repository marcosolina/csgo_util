package com.ixigo.demmanager.models.database;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

/**
 * DAO representing the DB table which contains the Steam Players info
 * 
 * @author marco
 *
 */
public class UsersDao extends IxigoDao<UsersDto> {

	private static final long serialVersionUID = 1L;
	private UsersDto dto = null;

	public UsersDao() {
		this.setSqlViewName("users");
		this.setSqlKeys(new String[] { UsersDto.Fields.steam_id });
		this.setSqlFields(new String[] { UsersDto.Fields.user_name, UsersDto.Fields.steam_id });
		this.dto = new UsersDto();
	}

	@Override
	public UsersDto mappingFunction(Row row, RowMetadata rowMetaData) {
		UsersDto dto = new UsersDto();
		dto.setSteam_id(row.get(UsersDto.Fields.steam_id, String.class));
		dto.setUser_name(row.get(UsersDto.Fields.user_name, String.class));
		return dto;
	}

	public String getUser_name() {
		return dto.getUser_name();
	}

	public void setUser_name(String user_name) {
		this.dto.setUser_name(user_name);
	}

	public String getSteam_id() {
		return dto.getSteam_id();
	}

	public void setSteam_id(String steam_id) {
		this.dto.setSteam_id(steam_id);
	}

	public UsersDto getDto() {
		return this.dto;
	}

	public void setDto(UsersDto dto) {
		this.dto = dto;
	}

}
