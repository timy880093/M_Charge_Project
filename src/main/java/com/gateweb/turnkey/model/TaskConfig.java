/*
 * $Header: $
 * This java source file is generated by pkliu on Fri Aug 11 14:13:12 CST 2017
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.turnkey.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.GenerationType.AUTO;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.meshinnovation.db.model.BaseObject;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *
 * @author pkliu
 *
 */
@Entity
@Table(name = "task_config")
public class TaskConfig extends BaseObject{

//long serialVersionUID jdk tool: serialver.exe 
	
	@EmbeddedId
    private TaskConfigKeys id;

	/**
	 * trans_chinese_date
	 */
	@Column(name = "trans_chinese_date")
	protected java.lang.String transChineseDate;
   
	/**
	 * src_path
	 */
	@Column(name = "src_path")
	protected java.lang.String srcPath;
	
	/**
	 * encoding
	 */
	@Column(name = "encoding")
	protected java.lang.String encoding;
	
	/**
	 * version
	 */
	@Column(name = "version")
	protected java.lang.String version;
	
	/**
	 * target_path
	 */
	@Column(name = "target_path")
	protected java.lang.String targetPath;
	
	/**
	 * file_format
	 */
	@Column(name = "file_format")
	protected java.lang.String fileFormat;
	



    
	/**
	 * 002
	 * @return java.lang.String transChineseDate
	 */
	public java.lang.String getTransChineseDate() {
		return this.transChineseDate;
	}
	
	/** 0001
	 * @param data Set the transChineseDate
	 */	
	public void setTransChineseDate(java.lang.String data) {
		this.transChineseDate = data;
	}	
	
	/**
	 * 002
	 * @return java.lang.String srcPath
	 */
	public java.lang.String getSrcPath() {
		return this.srcPath;
	}
	
	/** 0001
	 * @param data Set the srcPath
	 */	
	public void setSrcPath(java.lang.String data) {
		this.srcPath = data;
	}	
	/**
	 * 002
	 * @return java.lang.String encoding
	 */
	public java.lang.String getEncoding() {
		return this.encoding;
	}
	
	/** 0001
	 * @param data Set the encoding
	 */	
	public void setEncoding(java.lang.String data) {
		this.encoding = data;
	}	
	/**
	 * 002
	 * @return java.lang.String version
	 */
	public java.lang.String getVersion() {
		return this.version;
	}
	
	/** 0001
	 * @param data Set the version
	 */	
	public void setVersion(java.lang.String data) {
		this.version = data;
	}	
	/**
	 * 002
	 * @return java.lang.String targetPath
	 */
	public java.lang.String getTargetPath() {
		return this.targetPath;
	}
	
	/** 0001
	 * @param data Set the targetPath
	 */	
	public void setTargetPath(java.lang.String data) {
		this.targetPath = data;
	}	
	/**
	 * 002
	 * @return java.lang.String fileFormat
	 */
	public java.lang.String getFileFormat() {
		return this.fileFormat;
	}
	
	/** 0001
	 * @param data Set the fileFormat
	 */	
	public void setFileFormat(java.lang.String data) {
		this.fileFormat = data;
	}	

	/**
	 * @return the id
	 */
	public TaskConfigKeys getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(TaskConfigKeys id) {
		this.id = id;
	}

	/**
	 *
	 */
	public TaskConfig(){
	}

	/**
	 * full constructor 
	 * @param transChineseDate 
	 * @param task 
	 * @param categoryType 
	 * @param processType 
	 * @param srcPath 
	 * @param encoding 
	 * @param version 
	 * @param targetPath 
	 * @param fileFormat 
	 */
	public TaskConfig (
		 java.lang.String transChineseDate 
		, java.lang.String srcPath 
		, java.lang.String encoding 
		, java.lang.String version 
		, java.lang.String targetPath 
		, java.lang.String fileFormat 
		, TaskConfigKeys id
        ) {
		this.setTransChineseDate(transChineseDate);
		this.setId(id);
		this.setSrcPath(srcPath);
		this.setEncoding(encoding);
		this.setVersion(version);
		this.setTargetPath(targetPath);
		this.setFileFormat(fileFormat);
    }

	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		 //return ToStringBuilder.reflectionToString(this , ToStringStyle.MULTI_LINE_STYLE);  
		 return new ToStringBuilder(this)
		.append("transChineseDate", this.transChineseDate)
		.append("task", this.id.task)
		.append("categoryType", this.id.categoryType)
		.append("processType", this.id.processType)
		.append("srcPath", this.srcPath)
		.append("encoding", this.encoding)
		.append("version", this.version)
		.append("targetPath", this.targetPath)
		.append("fileFormat", this.fileFormat)
		.toString();
	}	 

	/*
    @Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof TaskConfig))
			return false;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(this.id, ((TaskConfig) object).id);
		return builder.isEquals();
	}*/
	
	/**
	 * Indicates whether some other object is "equal to" this one.
	 * @return true is equal, false is not equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null || !(obj instanceof TaskConfig))
			return false;

		TaskConfig key = (TaskConfig) obj;
		if (this.id.task != key.id.task ) 
        	return false;
		if (this.id.categoryType != key.id.categoryType ) 
        	return false;
		if (this.id.processType != key.id.processType ) 
        	return false;

		return true;
    }

}