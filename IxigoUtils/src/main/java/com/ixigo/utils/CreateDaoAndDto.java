package com.ixigo.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.marco.utils.DatabaseUtils;
import com.marco.utils.MarcoException;
import com.marco.utils.enums.DbType;

/**
 * Hello world!
 *
 */
public class CreateDaoAndDto {
	public static void main(String[] args) {

		CreateDaoAndDto test = new CreateDaoAndDto();
		String userName = "REPLACE_ME";
		String passw = "REPLACE_ME";
		String dbIp = "localhost";
		int dbPort = 5432;
		String dbName = "demfiles";
		
		DatabaseUtils.initialize(dbIp, dbPort, dbName, userName, passw, DbType.POSTGRES);
		
		Connection cn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			cn = DatabaseUtils.getInstance().createDbConnection();
			DatabaseMetaData dbmd = cn.getMetaData();
			
			rs = dbmd.getTables(cn.getCatalog(), cn.getSchema(), "%", null);
			while (rs.next()) {
				String tableName = rs.getString(3);
				if(!tableName.endsWith("_pkey") && !tableName.endsWith("_seq")) {					
					System.out.println(rs.getString(3));
					test.createDaoAndDto(tableName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtils.closeSqlObjects(cn, st, rs);
		}

	}

	public void createDaoAndDto(String tableName) throws Exception {
		Connection cn = null;
		Statement st = null;
		Statement st2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;

		PrintWriter daoWriter = null;

		try {
			cn = DatabaseUtils.getInstance().createDbConnection();

			Map<String, Column> keys = new HashMap<>();
			Map<String, Column> fields = new HashMap<>();

			DatabaseMetaData dbmd = cn.getMetaData();

			rs = dbmd.getPrimaryKeys(cn.getCatalog(), cn.getSchema(), tableName);
			st2 = cn.createStatement();
			while (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					if (rsmd.getColumnName(i + 1).equalsIgnoreCase("COLUMN_NAME")) {
						Column c = new Column();
						c.name = rs.getString(i + 1);
						
						rs2 = st2.executeQuery(String.format("select %s from %s", c.name, tableName));
						c.isAutoIncremental = rs2.getMetaData().isAutoIncrement(1);
						rs2.close();
						keys.put(c.name, c);
					}
				}
			}

			rs.close();
			rs = dbmd.getColumns(cn.getCatalog(), cn.getSchema(), tableName, "%");

			while (rs.next()) {
				Column c = new Column();
				c.name = rs.getString("COLUMN_NAME");
				c.type = rs.getString("TYPE_NAME").toUpperCase();
				fields.put(c.name, c);
				if (keys.get(c.name) != null) {
					c.key = true;
					c.isAutoIncremental = keys.get(c.name).isAutoIncremental;
					keys.put(c.name, c);
				}
				System.out.println(c.toString());
			}

			Set<String> k = fields.keySet();
			List<String> sbKeys = new ArrayList<>();
			List<String> sbFields = new ArrayList<>();
			for (String string : k) {
				Column c = fields.get(string);
				sbFields.add(c.name);
				if (c.key) {
					sbKeys.add(c.name);
				}
			}

			String fileName = tableName.toUpperCase().charAt(0) + tableName.substring(1);
			
			this.saveDao(fileName, tableName, sbKeys, sbFields, keys, fields);
			this.saveDto(fileName, fields);
			this.saveSvc(fileName, fields);
			this.saveRest(fileName, fields);

			System.out.println("###################### DONE refresh the project");
		} catch (FileNotFoundException | UnsupportedEncodingException | SQLException | MarcoException e) {
			throw e;
		} finally {
			DatabaseUtils.closeSqlObjects(cn, st, rs);
			DatabaseUtils.closeSqlObjects(null, st2, rs2);
			if (daoWriter != null) {
				daoWriter.close();
			}
		}
	}

	public String fieldName(String nc) {
		return nc.toUpperCase().charAt(0) + nc.substring(1);
	}
	
	private void saveDao(String fileName, String tableName, List<String> sbKeys, List<String> sbFields, Map<String, Column> keys, Map<String, Column> fields) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter daoWriter = null;
		try {
			String currentPackage = this.getClass().getPackage().getName();

			String path = "src/main/java/" + currentPackage.replaceAll("\\.", "/") + "/";

			/*
			 * ################################################# WRITING DAO
			 * #################################################
			 */

			daoWriter = new PrintWriter(path + fileName + "Dao.java", "UTF-8");
			daoWriter.println("package " + currentPackage + ";");
			daoWriter.println("");
			daoWriter.println("import java.util.Arrays;");
			daoWriter.println("import java.math.BigDecimal;");
			daoWriter.println("import org.slf4j.Logger;");
			daoWriter.println("import org.slf4j.LoggerFactory;");
			daoWriter.println("import com.ixigo.library.dao.IxigoDao;");
			daoWriter.println("");
			daoWriter.println("public class " + fileName + "Dao extends IxigoDao<" + fileName + "Dto> {");
			daoWriter.println("");
			daoWriter.println("\tprivate static final Logger _LOGGER = LoggerFactory.getLogger(" + fileName + "Dao.class);");
			daoWriter.println("\tprivate static final long serialVersionUID = 1L;");
			daoWriter.println("\tpublic static final String tableName = \"" + tableName + "\";");
			daoWriter.println("\tprivate " + fileName + "Dto dto = null;");
			daoWriter.println("");
			daoWriter.println("\tpublic " + fileName + "Dao() {");
			daoWriter.println(String.format("\t\t_LOGGER.trace(\"Instanciating %sDao\");", fileName));
			daoWriter.println("\t\tthis.setSqlViewName(tableName);");
			daoWriter.println("\t\t// @formatter:off");
			if (sbKeys.isEmpty()) {
				daoWriter.println("\t\tthis.setSqlKeys(new String[] {  });");
			} else {
				daoWriter.println("\t\tthis.setSqlKeys(new String[] {");
				for (String key : sbKeys) {
					daoWriter.println(String.format("\t\t\t%sDto.Fields.%s,", fileName, key));
				}
				daoWriter.println("\t\t});");
			}
			
			if (keys.isEmpty()) {
				daoWriter.println("\t\tthis.setSqlAutoincrementalFiles(new ArrayList<String>());");
			} else {
				daoWriter.println("\t\tthis.setSqlAutoincrementalFiles(Arrays.asList(new String[] {");
				for (var entry : keys.entrySet()) {
					Column c = entry.getValue();
					if(c.isAutoIncremental) {
						daoWriter.println(String.format("\t\t\t%sDto.Fields.%s,", fileName, entry.getValue().name));
					}
				}
				daoWriter.println("\t\t}));");
			}

			daoWriter.println("\t\tthis.setSqlFields(new String[] {");
			for (String field : sbFields) {
				daoWriter.println(String.format("\t\t\t%sDto.Fields.%s,", fileName, field));
			}
			daoWriter.println("\t\t});");
			daoWriter.println("\t\t// @formatter:on");

			daoWriter.println("\t\tthis.dto = new " + fileName + "Dto();");
			daoWriter.println("\t}");
			daoWriter.println("");
			daoWriter.println("\t@Override");
			daoWriter.println(String.format("\tpublic %sDto mappingFunction(Row row, RowMetadata rowMetaData) {", fileName));
			daoWriter.println("\t\t_LOGGER.trace(\"Mapping data\");");
			daoWriter.println(String.format("\t\treturn this.genericMappingFunction(new %sDto(), row, rowMetaData);", fileName));
			daoWriter.println("\t}");

			Set<String> k = fields.keySet();
			for (String string : k) {
				daoWriter.println("");
				Column c = fields.get(string);
				String capitalName = fieldName(c.name);

				String classType = "";

				switch (c.type) {
				case "TEXT":
				case "CHAR":
				case "VARCHAR":
				case "BPCHAR":
					classType = "String";
					break;
				case "DECIMAL":
				case "NUMERIC":
					classType = "BigDecimal";
					break;
				case "DATE":
					classType = "LocalDate";
					break;
				case "BOOL":
					classType = "Boolean";
					break;
				case "TIMESTAMP":
					classType = "LocalDateTime";
					break;
				case "INT4":
				case "INT8":
				case "SERIAL":
					classType = "Long";
					break;
				default:
					System.err.println(String.format("Column: %s of type: %s not manageg", c.name, c.type));
					break;
				}

				daoWriter.println("\tpublic " + classType + " get" + capitalName + "(){");
				daoWriter.println("\t\treturn dto.get" + capitalName + "();");
				daoWriter.println("\t}");
				daoWriter.println("");
				daoWriter.println("\tpublic void set" + capitalName + "(" + classType + " " + c.name + "){");
				// daoWriter.println("\t\tcheckChanged(this.dto.get" + capitalName + "(), " +
				// c.name + ");");
				daoWriter.println("\t\tthis.dto.set" + capitalName + "(" + c.name + ");");
				daoWriter.println("\t}");

				daoWriter.println("");
			}

			daoWriter.println("");
			daoWriter.println("\tpublic " + fileName + "Dto getDto(){");
			daoWriter.println("\t\treturn this.dto;");
			daoWriter.println("\t}");
			daoWriter.println("");
			daoWriter.println("\tpublic void setDto(" + fileName + "Dto dto){");
			daoWriter.println("\t\tthis.dto = dto;");
			daoWriter.println("\t}");

			daoWriter.println("");
			daoWriter.println("}");
		} finally {
			if (daoWriter != null) {
				daoWriter.close();
			}
		}
	}
	
	private void saveDto(String fileName, Map<String, Column> fields) throws IOException {
		PrintWriter dtoWriter = null;

		try {


			String currentPackage = this.getClass().getPackage().getName();

			String path = "src/main/java/" + currentPackage.replaceAll("\\.", "/") + "/";


			/*
			 * ################################################# WRITING DTO
			 * #################################################
			 */
			dtoWriter = new PrintWriter(path + fileName + "Dto.java", "UTF-8");
			dtoWriter.println("package " + currentPackage + ";");
			dtoWriter.println("");
			dtoWriter.println("import java.math.BigDecimal;");
			dtoWriter.println("import com.ixigo.library.dto.IxigoDto;");
			dtoWriter.println("");
			dtoWriter.println("@FieldNameConstants");
			dtoWriter.println("@Getter");
			dtoWriter.println("@Setter");
			dtoWriter.println("@Accessors(chain = true)");
			dtoWriter.println("public class " + fileName + "Dto implements IxigoDto {");
			dtoWriter.println("");
			dtoWriter.println("\tprivate static final long serialVersionUID = 1L;");
			Set<String> k = fields.keySet();
			for (String string : k) {
				Column c = fields.get(string);
				dtoWriter.print("\tprivate ");
				switch (c.type) {
				case "TEXT":
				case "CHAR":
				case "VARCHAR":
				case "BPCHAR":
					dtoWriter.println("String " + c.name + " = \"\";");
					break;
				case "DECIMAL":
				case "NUMERIC":
					dtoWriter.println("BigDecimal " + c.name + " = BigDecimal.ZERO;");
					break;
				case "DATE":
					dtoWriter.println("LocalDate " + c.name + " = null;");
					break;
				case "BOOL":
					dtoWriter.println("Boolean " + c.name + " = null;");
					break;
				case "TIMESTAMP":
					dtoWriter.println("LocalDateTime " + c.name + " = null;");
					break;
				case "INT4":
				case "INT8":
				case "SERIAL":
					dtoWriter.println("Long " + c.name + " = null;");
					break;
				default:
					System.err.println(String.format("Column: %s of type: %s not manageg", c.name, c.type));
					break;
				}
			}


			dtoWriter.println("");
			dtoWriter.println("}");

			System.out.println("###################### DONE refresh the project");
		} finally {
			if (dtoWriter != null) {
				dtoWriter.close();
			}
		}
	}
	
	private void saveSvc(String fileName, Map<String, Column> fields) throws IOException {
		PrintWriter dtoWriter = null;

		try {
			String svcFileName = "Svc" + Arrays.asList(fileName.toLowerCase().split("_")).stream().map(StringUtils::capitalize).collect(Collectors.joining()); 

			String currentPackage = this.getClass().getPackage().getName();

			String path = "src/main/java/" + currentPackage.replaceAll("\\.", "/") + "/";


			/*
			 * ################################################# WRITING DTO
			 * #################################################
			 */
			dtoWriter = new PrintWriter(path + svcFileName + ".java", "UTF-8");
			dtoWriter.println("package " + currentPackage + ";");
			dtoWriter.println("");
			dtoWriter.println("import java.math.BigDecimal;");
			dtoWriter.println("import com.ixigo.library.dto.IxigoDto;");
			dtoWriter.println("");
			dtoWriter.println("@FieldNameConstants");
			dtoWriter.println("@Getter");
			dtoWriter.println("@Setter");
			dtoWriter.println("@Accessors(chain = true)");
			dtoWriter.println("public class " + svcFileName + " implements IxigoDto {");
			dtoWriter.println("");
			dtoWriter.println("\tprivate static final long serialVersionUID = 1L;");
			Set<String> k = fields.keySet();
			for (String string : k) {
				Column c = fields.get(string);
				dtoWriter.print("\tprivate ");
				switch (c.type) {
				case "TEXT":
				case "CHAR":
				case "VARCHAR":
				case "BPCHAR":
					dtoWriter.println("String " + c.name + " = \"\";");
					break;
				case "DECIMAL":
				case "NUMERIC":
					dtoWriter.println("BigDecimal " + c.name + " = BigDecimal.ZERO;");
					break;
				case "DATE":
					dtoWriter.println("LocalDate " + c.name + " = null;");
					break;
				case "BOOL":
					dtoWriter.println("Boolean " + c.name + " = null;");
					break;
				case "TIMESTAMP":
					dtoWriter.println("LocalDateTime " + c.name + " = null;");
					break;
				case "INT4":
				case "INT8":
				case "SERIAL":
					dtoWriter.println("Long " + c.name + " = null;");
					break;
				default:
					System.err.println(String.format("Column: %s of type: %s not manageg", c.name, c.type));
					break;
				}
			}


			dtoWriter.println("");
			dtoWriter.println("}");

			System.out.println("###################### DONE refresh the project");
		} finally {
			if (dtoWriter != null) {
				dtoWriter.close();
			}
		}
	}
	
	private void saveRest(String fileName, Map<String, Column> fields) throws IOException {
		PrintWriter dtoWriter = null;

		try {
			String svcFileName = "Rest" + Arrays.asList(fileName.toLowerCase().split("_")).stream().map(StringUtils::capitalize).collect(Collectors.joining()); 

			String currentPackage = this.getClass().getPackage().getName();

			String path = "src/main/java/" + currentPackage.replaceAll("\\.", "/") + "/";


			/*
			 * ################################################# WRITING DTO
			 * #################################################
			 */
			dtoWriter = new PrintWriter(path + svcFileName + ".java", "UTF-8");
			dtoWriter.println("package " + currentPackage + ";");
			dtoWriter.println("");
			dtoWriter.println("import java.math.BigDecimal;");
			dtoWriter.println("import com.ixigo.library.dto.IxigoDto;");
			dtoWriter.println("");
			dtoWriter.println("@FieldNameConstants");
			dtoWriter.println("@Getter");
			dtoWriter.println("@Setter");
			dtoWriter.println("@Accessors(chain = true)");
			dtoWriter.println("public class " + svcFileName + " implements IxigoDto {");
			dtoWriter.println("");
			dtoWriter.println("\tprivate static final long serialVersionUID = 1L;");
			Set<String> k = fields.keySet();
			for (String string : k) {
				Column c = fields.get(string);
				dtoWriter.print("\tprivate ");
				switch (c.type) {
				case "TEXT":
				case "CHAR":
				case "VARCHAR":
				case "BPCHAR":
					dtoWriter.println("String " + c.name + " = \"\";");
					break;
				case "DECIMAL":
				case "NUMERIC":
					dtoWriter.println("BigDecimal " + c.name + " = BigDecimal.ZERO;");
					break;
				case "DATE":
					dtoWriter.println("LocalDate " + c.name + " = null;");
					break;
				case "BOOL":
					dtoWriter.println("Boolean " + c.name + " = null;");
					break;
				case "TIMESTAMP":
					dtoWriter.println("LocalDateTime " + c.name + " = null;");
					break;
				case "INT4":
				case "INT8":
				case "SERIAL":
					dtoWriter.println("Long " + c.name + " = null;");
					break;
				default:
					System.err.println(String.format("Column: %s of type: %s not manageg", c.name, c.type));
					break;
				}
			}


			dtoWriter.println("");
			dtoWriter.println("}");

			System.out.println("###################### DONE refresh the project");
		} finally {
			if (dtoWriter != null) {
				dtoWriter.close();
			}
		}
	}
}

class Column {
	public String name;
	public String type;
	public boolean key;
	public boolean isAutoIncremental;

	@Override
	public String toString() {
		return "Column [name=" + name + ", type=" + type + ", key=" + key + ", isAutoIncremental=" + isAutoIncremental + "]";
	}
}
