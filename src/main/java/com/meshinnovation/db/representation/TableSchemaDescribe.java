package com.meshinnovation.db.representation;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * This class represent table schema by given connection.
 * 
 * @author Linus
 *
 */
public class TableSchemaDescribe {
	
	/**
	 * @param con: db connection
	 * @return: Table schema representation
	 * @throws SQLException
	 */
	public Set<Table> exploreTable(Connection con) throws SQLException {
		Set<Table> tables = new LinkedHashSet<Table>();
		DatabaseMetaData dbmd = con.getMetaData();
		
		DatabaseEnum db;
		if(StringUtils.contains(dbmd.getDriverName(), "MySQL")){
			db = DatabaseEnum.MySQL;
		}else if(StringUtils.contains(dbmd.getDriverName(), "Oracle")){
			db = DatabaseEnum.Oracle;
		}else if(StringUtils.contains(dbmd.getDriverName(), "HSQL")){
			db = DatabaseEnum.HSQLDB;
		}else{
			db = DatabaseEnum.SQLServer;
		}
		
		String catalogName = db.retrieveCatalogName(dbmd);
		String schemaName = db.retrieveSchemaName(dbmd);
		
		ResultSet tableRs = dbmd.getTables(catalogName, schemaName, null, new String[]{"TABLE"});
		while(tableRs.next()){
			String catalog = tableRs.getString("TABLE_CAT");
			String schema = tableRs.getString("TABLE_SCHEM");
			String tableName = tableRs.getString("TABLE_NAME");
			Table table = new Table();
			table.setCatalog(catalog);
			table.setSchema(schema);
			table.setName(tableName);
			tables.add(table);
			
			ResultSet pkRs = dbmd.getPrimaryKeys(catalog, schema, tableName);
			while(pkRs.next()){
				PrimaryKey pk = new PrimaryKey();
				pk.setColumnName(pkRs.getString("COLUMN_NAME"));
				pk.setSeq(pkRs.getShort("KEY_SEQ"));
				pk.setPkName(pkRs.getString("PK_NAME"));
				table.getPrimaryKeys().add(pk);
			}
			pkRs.close();
			
			ResultSet columnRs = dbmd.getColumns(catalog, schema, tableName, null);
			while(columnRs.next()){
				Column column = new Column();
				column.setName(columnRs.getString("COLUMN_NAME"));
				column.setDataType(columnRs.getInt("DATA_TYPE"));
				column.setTypeName(columnRs.getString("TYPE_NAME"));
				column.setSize(columnRs.getInt("COLUMN_SIZE"));
				column.setDecimailDigits(columnRs.getInt("DECIMAL_DIGITS"));
				column.setNullable(columnRs.getInt("NULLABLE"));
				column.setRemarks(columnRs.getString("REMARKS"));
				column.setDefaultValue(columnRs.getString("COLUMN_DEF"));
				column.setOrdinalPosition(columnRs.getInt("ORDINAL_POSITION"));
				column.setIsNullable(columnRs.getString("IS_NULLABLE"));
				table.getColumns().add(column);
			}
			columnRs.close();
		}
		tableRs.close();
		return tables;
	}
	
}
