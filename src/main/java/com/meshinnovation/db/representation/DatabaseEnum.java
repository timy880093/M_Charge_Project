package com.meshinnovation.db.representation;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

public enum DatabaseEnum {

	Oracle {
		@Override
		public String retrieveCatalogName(DatabaseMetaData dbmd) throws SQLException {
			return dbmd.getUserName();
		}

		@Override
		public String retrieveSchemaName(DatabaseMetaData dbmd) throws SQLException {
			return dbmd.getUserName();
		}
	}, SQLServer {
		@Override
		public String retrieveCatalogName(DatabaseMetaData dbmd) throws SQLException {
			return dbmd.getUserName();
		}

		@Override
		public String retrieveSchemaName(DatabaseMetaData dbmd) {
			return null;
		}
	}, HSQLDB {
		@Override
		public String retrieveCatalogName(DatabaseMetaData dbmd) {
			return "PUBLIC";
		}

		@Override
		public String retrieveSchemaName(DatabaseMetaData dbmd) {
			return null;
		}
	}, MySQL {
		@Override
		public String retrieveCatalogName(DatabaseMetaData dbmd) {
			return null;
		}

		@Override
		public String retrieveSchemaName(DatabaseMetaData dbmd) throws SQLException {
			return StringUtils.substringBefore(dbmd.getUserName(), "@");
		}
	};
	
	public abstract String retrieveCatalogName(DatabaseMetaData dbmd) throws SQLException;
	
	public abstract String retrieveSchemaName(DatabaseMetaData dbmd) throws SQLException;
}
