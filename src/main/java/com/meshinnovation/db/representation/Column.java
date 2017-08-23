package com.meshinnovation.db.representation;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class represent Column Metadata.
 * 
 * @author Linus
 *
 */
public class Column implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Integer dataType;
	private String typeName;
	private Integer size;
	private Integer decimailDigits;
	private Integer nullable;
	private String remarks;
	private String defaultValue;
	private Integer ordinalPosition;
	private String isNullable;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getDecimailDigits() {
		return decimailDigits;
	}
	public void setDecimailDigits(Integer decimailDigits) {
		this.decimailDigits = decimailDigits;
	}
	public Integer getNullable() {
		return nullable;
	}
	public void setNullable(Integer nullable) {
		this.nullable = nullable;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public Integer getOrdinalPosition() {
		return ordinalPosition;
	}
	public void setOrdinalPosition(Integer ordinalPosition) {
		this.ordinalPosition = ordinalPosition;
	}
	public String getIsNullable() {
		return isNullable;
	}
	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("name", name).append("dataType", dataType).append(
						"typeName", typeName).append("size", size).append(
						"decimailDigits", decimailDigits).append("nullable",
						nullable).append("remarks", remarks).append(
						"defaultValue", defaultValue).append("ordinalPosition",
						ordinalPosition).append("isNullable", isNullable)
				.toString();
	}
}
