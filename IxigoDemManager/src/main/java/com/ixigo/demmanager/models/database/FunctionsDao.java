package com.ixigo.demmanager.models.database;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixigo.library.dao.IxigoDao;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public class FunctionsDao extends IxigoDao<FunctionsDto> {
	private static final Logger _LOGGER = LoggerFactory.getLogger(FunctionsDao.class);
	private static final long serialVersionUID = 1L;
	public static final String tableName = "information_schema.routines";
	private FunctionsDto dto = null;

	public FunctionsDao() {
		_LOGGER.trace("Instanciating FunctionsDao");
		this.setSqlViewName(tableName);
		// @formatter:off
		this.setSqlKeys(new String[] {  });
		this.setSqlAutoincrementalFiles(new ArrayList<String>());
		this.setSqlFields(new String[] {
				FunctionsDto.Fields.routine_name,
		});
		// @formatter:on
		this.dto = new FunctionsDto();
	}

	@Override
	public FunctionsDto mappingFunction(Row row, RowMetadata rowMetaData) {
		_LOGGER.trace("Mapping data");
		return this.genericMappingFunction(new FunctionsDto(), row, rowMetaData);
	}

	public FunctionsDto getDto() {
		return dto;
	}

	@Override
	public void setDto(FunctionsDto dto) {
		this.dto = dto;
	}

}
