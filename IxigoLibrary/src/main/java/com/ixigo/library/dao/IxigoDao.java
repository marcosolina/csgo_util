package com.ixigo.library.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.ixigo.library.dto.IxigoDto;
import com.ixigo.library.enums.DateFormats;
import com.ixigo.library.errors.IxigoException;
import com.ixigo.library.utils.DateUtils;
import com.ixigo.library.utils.UtilsConstants;

/**
 * Data Access Object (DAO) used to access the data in the database
 * 
 * @see https://www.baeldung.com/spring-data-r2dbc
 * @see https://www.baeldung.com/r2dbc
 * @author Marco
 *
 */
public class IxigoDao implements Serializable, Cloneable {
	private static final Logger _LOGGER = LoggerFactory.getLogger(IxigoDao.class);
	private static final long serialVersionUID = 1L;

	private String sqlViewName = null;

	private String[] sqlFields = null;
	private String[] sqlKeys = null;

	/*
	 * field defined as key, but is calculated by the DB. For example
	 * autoincremental ID
	 */
	private String[] sqlAutoKeys = null;

	private List<String> sqlOrderFields = null;
	private List<String> sqlWhereAndClauses = null;

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

	public StringBuffer getSqlSelect() {
		StringBuffer sqlSt = new StringBuffer();
		StringBuffer f = new StringBuffer();
		sqlSt.append("select ");

		if (sqlFields != null && sqlFields.length > 0) {
			for (int i = 0; i < sqlFields.length; i++) {
				f.append(", " + sqlFields[i]);
			}
		} else {
			f.append(", *");
		}
		sqlSt.append(f.substring(2));

		sqlSt.append(" from ");
		sqlSt.append(sqlViewName);

		if (sizeSqlWhereAndClauses() > 0) {
			sqlSt.append(" where ");
			f = new StringBuffer();
			for (int i = 0; i < sizeSqlWhereAndClauses(); i++) {
				var field = getSqlWhereAndClauses(i);
				f.append(" and " + field + "=:" + field);
			}
			sqlSt.append(f.substring(4));
		}

		if (sizeSqlOrderFields() > 0) {
			sqlSt.append(" order by ");
			f = new StringBuffer();
			for (int i = 0; i < sizeSqlOrderFields(); i++) {
				f.append(", " + getSqlOrderFields(i));
			}
			sqlSt.append(f.substring(2));
		} else if (getSqlKeys() != null && getSqlKeys().length > 0) {
			sqlSt.append(" order by ");
			f = new StringBuffer();
			for (int i = 0; i < getSqlKeys().length; i++) {
				f.append(", " + getSqlKeys()[i]);
			}
			sqlSt.append(f.substring(2));
		}

		return sqlSt;
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

	/**
	 * It creates the delete statement
	 * 
	 * @return
	 */
	public StringBuffer getSqlDelete() {
		StringBuffer k = new StringBuffer();
		StringBuffer res = new StringBuffer("delete from ");
		res.append(sqlViewName);

		if (sqlKeys != null) {
			for (int i = 0; i < sqlKeys.length; i++) {
				k.append(" and " + sqlKeys[i] + " = ?");
			}
			if (sqlKeys.length > 0) {
				res.append(" where ");
				res.append(k.substring(5));
			}
		}
		return res;
	}

	/**
	 * It creates the INSERT SQL statement
	 * 
	 * @param dto
	 * @return
	 * @throws MarcoException
	 */
	public StringBuffer getSqlInsert(IxigoDto dto) throws IxigoException {
		StringBuffer res = getSqlInsert();
		int index = 0;
		for (int i = 0; i < sqlFields.length; i++) {
			if (isAutoGeneratedField(sqlFields[i])) {
				continue;
			}
			index = res.indexOf("?");
			String valore = getValueAsString(dto, sqlFields[i]);
			res.replace(index, index + 1, valore);
		}
		return resetString(res);
	}

	public StringBuffer getSqlUpdate() throws IxigoException {
		IxigoDto dto = getDtoInstance();
		StringBuffer res = getSqlUpdatePS();
		int index = 0;
		for (int i = 0; i < sqlFields.length; i++) {
			if (isAutoGeneratedField(sqlFields[i])) {
				continue;
			}
			index = res.indexOf("?");
			String valore = getValueAsString(dto, sqlFields[i]);
			res.replace(index, index + 1, valore);
			index += valore.length();
		}
		for (int i = 0; i < sqlKeys.length; i++) {
			index = res.indexOf("?");
			String valore = getValueAsString(dto, sqlKeys[i]);
			res.replace(index, index + 1, valore);
			index += valore.length();
		}
		return resetString(res);
	}

	/**
	 * Sets the values of the keys inside th DTO the input array must have the
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

	public String getterName(String nc) {
		return "get" + fieldName(nc);
	}

	public String setterName(String nc) {
		return "set" + fieldName(nc);
	}

	public String fieldName(String nc) {
		return nc.toUpperCase().charAt(0) + nc.substring(1);
	}

	private StringBuffer resetString(StringBuffer res) {
		return new StringBuffer(res.toString().replace(UtilsConstants.DUMMY_CHAR, '?'));
	}

	public int sizeSqlWhereAndClauses() {
		return sqlWhereAndClauses == null ? 0 : sqlWhereAndClauses.size();
	}

	private String getValueAsString(IxigoDto dto, String field) {
		String res = "null";
		try {
			Method m = dto.getClass().getMethod(getterName(field));
			Object value = m.invoke(dto);
			String classN = m.getReturnType().getName();
			if (value == null) {
				res = "NULL";
			} else if (classN.equals("java.lang.String")) {
				res = "'" + ((String) value).replaceAll("'", "''") + "'";
			} else if (classN.equals("java.math.BigDecimal")) {
				res = ((BigDecimal) value).toString();
			} else if (classN.equals("java.time.LocalDate")) {
				String s = DateUtils.fromLocalDateToString((LocalDate) value, DateFormats.DB_DATE);
				res = "'" + s + "'";
			} else if (classN.equals("java.time.LocalDateTime")) {
				String s = DateUtils.fromLocalDateTimeToString((LocalDateTime) value, DateFormats.DB_TIME_STAMP);
				res = "'" + s + "'";
			} else {

			}
		} catch (Exception e) {
			_LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		return res.replace('?', UtilsConstants.DUMMY_CHAR);
	}

	private StringBuffer getSqlInsert() throws IxigoException {
		StringBuffer fields = new StringBuffer();
		StringBuffer values = new StringBuffer();
		if (sqlFields == null) {
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, "Fields not defined");
		}
		for (int i = 0; i < sqlFields.length; i++) {
			fields.append(", " + sqlFields[i]);
			values.append(", ?");
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

	private StringBuffer getSqlUpdatePS() throws IxigoException {
		if (sqlKeys == null) {
			throw new IxigoException(HttpStatus.INTERNAL_SERVER_ERROR, "Can not update table without keys");
		}
		StringBuffer fields = new StringBuffer();
		for (int i = 0; i < sqlFields.length; i++) {
			fields.append(", " + sqlFields[i] + " = ?");
		}
		StringBuffer keys = new StringBuffer();
		for (int i = 0; i < sqlKeys.length; i++) {
			keys.append(" and " + sqlKeys[i] + " = ?");
		}
		StringBuffer res = new StringBuffer("update ");
		res.append(sqlViewName);
		res.append(" set ");
		res.append(fields.substring(2));
		if (sqlKeys.length > 0) {
			res.append(" where ");
			res.append(keys.substring(5));
		}
		return res;
	}

	private boolean isAutoGeneratedField(String fieldName) {
		boolean founded = false;
		for (int i = 0; sqlAutoKeys != null && i < sqlAutoKeys.length && !founded; i++) {
			if (sqlAutoKeys[i].equals(fieldName)) {
				founded = true;
			}
		}
		return founded;
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

	public String[] getSqlAutoKeys() {
		return sqlAutoKeys;
	}

	public void setSqlAutoKeys(String[] sqlAutoKeys) {
		this.sqlAutoKeys = sqlAutoKeys;
	}

	public void setSqlViewName(String sqlViewName) {
		this.sqlViewName = sqlViewName;
	}

	public void setSqlKeys(String[] sqlKeys) {
		this.sqlKeys = sqlKeys;
	}

	public void setSqlFields(String[] sqlFields) {
		this.sqlFields = sqlFields;
	}
}
