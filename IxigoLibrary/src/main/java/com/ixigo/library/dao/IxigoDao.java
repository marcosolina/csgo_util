package com.ixigo.library.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.DatabaseClient.GenericExecuteSpec;

import com.ixigo.library.dto.IxigoDto;
import com.ixigo.library.errors.IxigoException;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

/**
 * Data Access Object (DAO) used to access the data in the database
 * 
 * @see https://medium.com/swlh/working-with-relational-database-using-r2dbc-databaseclient-d61a60ebc67f
 * @see https://github.com/hantsy/spring-r2dbc-sample/blob/master/docs/database-client.md
 * @see https://medium.com/zero-equals-false/dealing-with-postgres-specific-json-enum-type-and-notifier-listener-with-r2dbc-f15cc104aa10
 * @see https://www.baeldung.com/spring-data-r2dbc
 * @see https://www.baeldung.com/r2dbc 
 * @author Marco
 *
 */
public abstract class IxigoDao<T extends IxigoDto> implements Serializable, Cloneable {
	private static final Logger _LOGGER = LoggerFactory.getLogger(IxigoDao.class);
	private static final long serialVersionUID = 1L;

	private String sqlViewName = null;

	private String[] sqlFields = null;
	private String[] sqlKeys = null;

	private List<String> sqlOrderFields = null;
	private List<String> sqlWhereAndClauses = null;
	private List<Object> sqlParams = null;

	/**
	 * Mapping function to map a single DB row into a Dto
	 * 
	 * @param row
	 * @param rowMetaData
	 * @return
	 */
	public abstract T mappingFunction(Row row, RowMetadata rowMetaData);

	/**
	 * It returns a deep copy of the DTO
	 */
	@Override
	public Object clone() {
		IxigoDto dto = getDtoInstance();
		IxigoDto clone;

		try {
			clone = dto.getClass().getDeclaredConstructor().newInstance();
			Method[] m = dto.getClass().getMethods();
			for (int j = 0; j < m.length; j++) {
				if (m[j].getName().startsWith("set") && m[j].getParameterTypes().length == 1) {
					m[j].invoke(clone, new Object[] { dto.getClass().getMethod(m[j].getName().replaceFirst("set", "get")).invoke(dto) });
				}
			}
			return clone;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			_LOGGER.error(e.getMessage());
			if (_LOGGER.isTraceEnabled()) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * It prepares the {@link GenericExecuteSpec} for the "SELECT" statement
	 * 
	 * SELECT FIELD_1, ..., FIELD_N FROM TABLE [WHERE FIELD_1 = :VAL_1 AND ...
	 * FIELD_N = :VAL_N] [ORDER BY FIELD_1, ..., FIELD_N]
	 * 
	 * @param client
	 * @return
	 */
	public GenericExecuteSpec prepareSqlSelect(DatabaseClient client) {
		StringBuffer sqlSelect = new StringBuffer();
		StringBuffer fields = new StringBuffer();
		sqlSelect.append("select ");

		if (sqlFields != null && sqlFields.length > 0) {
			for (int i = 0; i < sqlFields.length; i++) {
				fields.append(", " + sqlFields[i]);
			}
		} else {
			fields.append(", *");
		}
		sqlSelect.append(fields.substring(2));

		sqlSelect.append(" from ");
		sqlSelect.append(sqlViewName);
		if (sizeSqlWhereAndClauses() > 0) {
			sqlSelect.append(" where ");
			fields = new StringBuffer();
			for (int i = 0; i < sizeSqlWhereAndClauses(); i++) {
				var fieldName = getSqlWhereAndClauses(i);
				fields.append(" and " + fieldName + "=:" + fieldName);
			}
			sqlSelect.append(fields.substring(4));
		}

		if (sizeSqlOrderFields() > 0) {
			sqlSelect.append(" order by ");
			fields = new StringBuffer();
			for (int i = 0; i < sizeSqlOrderFields(); i++) {
				fields.append(", " + getSqlOrderFields(i));
			}
			sqlSelect.append(fields.substring(2));
		} else if (getSqlKeys() != null && getSqlKeys().length > 0) {
			sqlSelect.append(" order by ");
			fields = new StringBuffer();
			for (int i = 0; i < getSqlKeys().length; i++) {
				fields.append(", " + getSqlKeys()[i]);
			}
			sqlSelect.append(fields.substring(2));
		}

		GenericExecuteSpec ges = client.sql(sqlSelect.toString());
		for (int i = 0; i < sizeSqlParams(); i++) {
			var paramValue = getSqlParams(i);
			ges = ges.bind(i, paramValue.getClass().isEnum() ? paramValue.toString() : paramValue);
		}

		return ges;
	}

	/**
	 * It prepares the {@link GenericExecuteSpec} for the "DELETE" statement by
	 * table key
	 * 
	 * @return
	 */
	public GenericExecuteSpec prepareSqlDeleteByKey(DatabaseClient client) {
		StringBuffer keyFields = new StringBuffer();
		StringBuffer sqlDelete = new StringBuffer("delete from ");
		sqlDelete.append(sqlViewName);

		if (sqlKeys != null) {
			for (int i = 0; i < sqlKeys.length; i++) {
				keyFields.append(" and " + sqlKeys[i] + " = :" + sqlKeys[i]);
			}
			if (sqlKeys.length > 0) {
				sqlDelete.append(" where ");
				sqlDelete.append(keyFields.substring(5));
			}
		}

		_LOGGER.debug(sqlDelete.toString());
		GenericExecuteSpec ges = client.sql(sqlDelete.toString());
		if (sqlKeys != null && sqlKeys.length > 0) {
			var dto = this.getDtoInstance();
			for (int i = 0; i < sqlKeys.length; i++) {
				var paramValue = getValue(dto, sqlKeys[i]);
				ges = ges.bind(i, paramValue.getClass().isEnum() ? paramValue.toString() : paramValue);
			}
		}

		return ges;
	}
	
	/**
	 * It prepares the {@link GenericExecuteSpec} for the "SELECT" statement by
	 * table key
	 * 
	 * @return
	 */
	public GenericExecuteSpec prepareSqlSelectByKey(DatabaseClient client) {
		StringBuffer keyFields = new StringBuffer();
		StringBuffer sqlSelect = new StringBuffer("select * from ");
		sqlSelect.append(sqlViewName);

		if (sqlKeys != null) {
			for (int i = 0; i < sqlKeys.length; i++) {
				keyFields.append(" and " + sqlKeys[i] + " = :" + sqlKeys[i]);
			}
			if (sqlKeys.length > 0) {
				sqlSelect.append(" where ");
				sqlSelect.append(keyFields.substring(5));
			}
		}

		GenericExecuteSpec ges = client.sql(sqlSelect.toString());
		if (sqlKeys != null && sqlKeys.length > 0) {
			var dto = this.getDtoInstance();
			for (int i = 0; i < sqlKeys.length; i++) {
				var paramValue = getValue(dto, sqlKeys[i]);
				ges = ges.bind(i, paramValue.getClass().isEnum() ? paramValue.toString() : paramValue);
			}
		}

		return ges;
	}

	/**
	 * It prepares the {@link GenericExecuteSpec} for the "INSERT" statement into
	 * the table
	 * 
	 * @param dto
	 * @return
	 * @throws MarcoException
	 */
	public GenericExecuteSpec prepareSqlInsert(DatabaseClient client) {
		var insert = getSqlInsert().toString();
		_LOGGER.debug(insert);

		var ges = client.sql(insert);
		var dto = this.getDtoInstance();
		for (int i = 0; i < sqlFields.length; i++) {
			Object value = getValue(dto, sqlFields[i]);
			if(value == null) {
				ges = ges.bindNull(sqlFields[i], getDataType(dto, sqlFields[i]));
			}else {
				ges = ges.bind(sqlFields[i], value.getClass().isEnum() ? value.toString() : value);
			}
		}
		return ges;
	}

	/**
	 * Sets the values of the keys inside the DTO. The input array must have the
	 * values in the same order of the fields defined as key fields
	 * 
	 * @param keyValues
	 */
	public void setKeyValues(Object[] keyValues) {
		IxigoDto dto = getDtoInstance();
		try {
			for (int i = 0; i < sqlKeys.length; i++) {
				@SuppressWarnings("rawtypes")
				Class keyClass = dto.getClass().getMethod(getterName(sqlKeys[i]), (Class[]) null).getReturnType();
				dto.getClass().getMethod(setterName(sqlKeys[i]), new Class[] { keyClass }).invoke(dto, new Object[] { keyValues[i] });
			}
		} catch (Exception e) {
			_LOGGER.error(e.getMessage());
		}
	}

	/**
	 * Adds the parameter to the list of parameters that are going to be used with
	 * the prepared statement. You should use this combined with the add where and
	 * clauses method
	 * 
	 * For example: dao.addSqlWhereAndClauses("field1 = ?");
	 * dao.addSqlWhereAndClauses("field2 = ?");
	 * 
	 * dao.addSqlParams("value1"); dao.addSqlParams("value2");
	 * 
	 * dao.doSelect(cn);
	 * 
	 * 
	 * @param o
	 * @return
	 */
	public boolean addSqlParams(Object o) {
		if (sqlParams == null) {
			sqlParams = new ArrayList<Object>();
		}
		return sqlParams.add(o);
	}

	public String getterName(String nc) {
		return "get" + fieldName(nc);
	}

	public String setterName(String nc) {
		return "set" + fieldName(nc);
	}

	public String fieldName(String nc) {
		return nc.toUpperCase().charAt(0) + nc.substring(1);
	}

	public int sizeSqlWhereAndClauses() {
		return sqlWhereAndClauses == null ? 0 : sqlWhereAndClauses.size();
	}

	private Object getValue(IxigoDto dto, String field) {
		try {
			Method m = dto.getClass().getMethod(getterName(field));
			Object value = m.invoke(dto);
			if (value == null) {
				return null;
			}
			if (m.getReturnType().isEnum()) {
				return value.toString();
			}
			return value;
		} catch (Exception e) {
			_LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	private Class<?> getDataType(IxigoDto dto, String field) {
		try {
			return dto.getClass().getMethod(getterName(field)).getReturnType(); 
		} catch (Exception e) {
			_LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private StringBuffer getSqlInsert() throws IxigoException {
		StringBuffer fields = new StringBuffer();
		StringBuffer values = new StringBuffer();
		if (sqlFields == null) {
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, "Fields not defined");
		}
		for (int i = 0; i < sqlFields.length; i++) {
			fields.append(", " + sqlFields[i]);
			values.append(", :" + sqlFields[i]);
		}
		StringBuffer res = new StringBuffer("insert into ");
		res.append(sqlViewName);
		res.append(" (");
		res.append(fields.substring(2));
		res.append(") values (");
		res.append(values.substring(2));
		res.append(")");
		return res;
	}

	public GenericExecuteSpec prepareSqlUpdate(DatabaseClient client)  throws IxigoException {
		if (sqlKeys == null) {
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, "Can not update table without keys");
		}
		StringBuffer fields = new StringBuffer();
		for (int i = 0; i < sqlFields.length; i++) {
			fields.append(", " + sqlFields[i] + " =:" + sqlFields[i]);
		}
		StringBuffer keys = new StringBuffer();
		for (int i = 0; i < sqlKeys.length; i++) {
			keys.append(" and " + sqlKeys[i] + " =:" + sqlKeys[i]);
		}
		StringBuffer res = new StringBuffer("update ");
		res.append(sqlViewName);
		res.append(" set ");
		res.append(fields.substring(2));
		if (sqlKeys.length > 0) {
			res.append(" where ");
			res.append(keys.substring(5));
		}
		
		var ges = client.sql(res.toString());
		var dto = this.getDtoInstance();
		for (int i = 0; i < sqlFields.length; i++) {
			Object value = getValue(dto, sqlFields[i]);
			if(value == null) {
				ges = ges.bindNull(sqlFields[i], getDataType(dto, sqlFields[i]));
			}else {
				ges = ges.bind(sqlFields[i], value.getClass().isEnum() ? value.toString() : value);
			}
		}
		
		return ges;
	}

	/**
	 * It returns the DTO instance of the current DAO
	 * 
	 * @return
	 */
	private IxigoDto getDtoInstance() {
		IxigoDto dto = null;
		try {
			dto = (IxigoDto) this.getClass().getMethod("getDto").invoke((Object) this);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			_LOGGER.error(e.getMessage());
			if (_LOGGER.isTraceEnabled()) {
				e.printStackTrace();
			}
		}

		return dto;
	}

	public void setSqlViewName(String sqlViewName) {
		this.sqlViewName = sqlViewName;
	}

	public void setSqlKeys(String[] sqlKeys) {
		this.sqlKeys = sqlKeys;
	}

	protected void setSqlFields(String[] sqlFields) {
		this.sqlFields = sqlFields;
	}
	
	protected String[] getSqlFields() {
		return this.sqlFields;
	}

	public boolean setSqlOrderFields(List<String> o) {
		sqlOrderFields = o;
		return true;
	}

	public boolean addSqlOrderFields(String o) {
		if (sqlOrderFields == null) {
			sqlOrderFields = new ArrayList<>();
		}
		return sqlOrderFields.add(o);
	}

	/**
	 * adds the string to the list of the "and" fields of the where clauses
	 * 
	 * where field1 = '' and field2 = '' and ...
	 * 
	 * @param element
	 */
	public void addSqlWhereAndClauses(String element) {
		if (sqlWhereAndClauses == null) {
			sqlWhereAndClauses = new ArrayList<>();
		}
		sqlWhereAndClauses.add(element);
	}

	/**
	 * It clears the list of the "and" fields of the where clauses
	 */
	public void clearSqlOrderFields() {
		if (sqlOrderFields == null) {
			sqlOrderFields = new ArrayList<>();
		} else {
			sqlOrderFields.clear();
		}
	}

	/**
	 * It clears the sql where and clauses list
	 */
	public void clearSqlWhereAndClauses() {
		if (sqlWhereAndClauses == null) {
			sqlWhereAndClauses = new ArrayList<>();
		} else {
			sqlWhereAndClauses.clear();
		}
	}

	public int sizeSqlParams() {
		return sqlParams == null ? 0 : sqlParams.size();
	}

	public Object getSqlParams(int index) {
		return sqlParams == null ? null : sqlParams.get(index);
	}

	public String[] getSqlKeys() {
		return sqlKeys;
	}

	public String getSqlOrderFields(int index) {
		return sqlOrderFields == null ? null : sqlOrderFields.get(index);
	}

	public List<String> getSqlOrderFields() {
		return sqlOrderFields;
	}

	public Object getSqlWhereAndClauses(int index) {
		return sqlWhereAndClauses == null ? null : sqlWhereAndClauses.get(index);
	}

	public int sizeSqlOrderFields() {
		return sqlOrderFields == null ? 0 : sqlOrderFields.size();
	}
}
