package com.meshinnovation.db.representation;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class represent Table Metadata.
 * 
 * @author Linus
 *
 */
public class Table implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String catalog;
	private String schema;
	private String name;
	private Set<PrimaryKey> primaryKeys = new LinkedHashSet<PrimaryKey>();
	private Set<Column> columns = new LinkedHashSet<Column>();
	
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<PrimaryKey> getPrimaryKeys() {
		return primaryKeys;
	}
	public String getPrimaryKeyColumns(){
		StringBuffer sb = new StringBuffer();
		for(Iterator<PrimaryKey> i = primaryKeys.iterator(); i.hasNext(); ){
			PrimaryKey pk = i.next();
			sb.append(pk.getColumnName());
			if(i.hasNext()){
				sb.append("、");
			}
		}
		return sb.toString();
	}
	public Set<Column> getColumns() {
		return columns;
	}
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("catalog", catalog).append("schema", schema).append(
						"name", name).toString();
	}
}
