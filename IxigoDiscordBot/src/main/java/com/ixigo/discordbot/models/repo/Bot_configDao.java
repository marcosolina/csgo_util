package com.ixigo.discordbot.models.repo;

import com.ixigo.enums.BotConfigKey;
import com.ixigo.enums.BotConfigValueType;
import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class Bot_configDao extends IxigoDao<Bot_configDto> {

	private static final long serialVersionUID = 1L;
	private Bot_configDto dto = null;

	public Bot_configDao() {
		this.setSqlViewName("bot_config");
		this.setSqlKeys(new String[] { Bot_configDto.Fields.config_key });
		this.setSqlFields(new String[] { Bot_configDto.Fields.config_key, Bot_configDto.Fields.config_val, Bot_configDto.Fields.config_value_type });
		this.dto = new Bot_configDto();
	}

	@Override
	public Bot_configDto mappingFunction(Row row, RowMetadata rowMetaData) {
		Bot_configDto dto = new Bot_configDto();
		dto.setConfig_key(BotConfigKey.valueOf(row.get(Bot_configDto.Fields.config_key, String.class)));
		dto.setConfig_value_type(BotConfigValueType.valueOf(row.get(Bot_configDto.Fields.config_value_type, String.class)));
		dto.setConfig_val(row.get(Bot_configDto.Fields.config_val, String.class));
		return dto;
	}

	public String getConfig_val() {
		return dto.getConfig_val();
	}

	public void setConfig_val(String config_val) {
		this.dto.setConfig_val(config_val);
	}

	public BotConfigKey getConfig_key() {
		return dto.getConfig_key();
	}

	public void setConfig_key(BotConfigKey config_key) {
		this.dto.setConfig_key(config_key);
	}
	
	public BotConfigValueType getConfig_value_type() {
		return dto.getConfig_value_type();
	}

	public void setConfig_key(BotConfigValueType config_value_type) {
		this.dto.setConfig_value_type(config_value_type);
	}

	public Bot_configDto getDto() {
		return this.dto;
	}

	public void setDto(Bot_configDto dto) {
		this.dto = dto;
	}

}
