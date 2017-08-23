/**
 * 
 */
package com.gateweb.turnkey.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author mac
 *
 */
@Embeddable
public class TaskConfigKeys implements java.io.Serializable{


	 /**
     * task java.lang.String , PK     * @GeneratedValue(strategy = GenerationType.AUTO)
     */
	@Column(name = "task", nullable = false)
	protected java.lang.String task;
	
	 /**
     * category_type java.lang.String , PK     * @GeneratedValue(strategy = GenerationType.AUTO)
     */
	@Column(name = "category_type", nullable = false)
	protected java.lang.String categoryType;
	
	 /**
     * process_type java.lang.String , PK     * @GeneratedValue(strategy = GenerationType.AUTO)
     */
	@Column(name = "process_type", nullable = false)
	protected java.lang.String processType;
	
	
	public TaskConfigKeys(){
		
	}
	
	public TaskConfigKeys(String task, String categoryType, String processType){
		super();
		this.task = task;
		this.categoryType = categoryType;
		this.processType = processType;
	}

	/**
	 * @return the task
	 */
	public java.lang.String getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(java.lang.String task) {
		this.task = task;
	}

	/**
	 * @return the categoryType
	 */
	public java.lang.String getCategoryType() {
		return categoryType;
	}

	/**
	 * @param categoryType the categoryType to set
	 */
	public void setCategoryType(java.lang.String categoryType) {
		this.categoryType = categoryType;
	}

	/**
	 * @return the processType
	 */
	public java.lang.String getProcessType() {
		return processType;
	}

	/**
	 * @param processType the processType to set
	 */
	public void setProcessType(java.lang.String processType) {
		this.processType = processType;
	}


}
