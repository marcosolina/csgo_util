package com.ixigo.demmanager.models.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class UsersDao extends IxigoDao<UsersDto> {

	private static final Logger _LOGGER = LoggerFactory.getLogger(UsersDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "users";
	private UsersDto dto = null;

	public UsersDao() {
		_LOGGER.trace("Instanciating UsersDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {
			UsersDto.Fields.steam_id,
		});
		this.setSqlFields(new String[] {
			UsersDto.Fields.user_name,
			UsersDto.Fields.steam_id,
		});
		// @formatter:on
		this.dto = new UsersDto();
	}

	@Override
	public UsersDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new UsersDto(), row, rowMetaData);
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
