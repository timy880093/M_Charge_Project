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
public class ArchivedfileKeys implements java.io.Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "filetype", nullable = false)
	protected java.lang.String filetype;
	
	@Column(name = "fileid", nullable = false)
	protected java.lang.String fileid;
	
	public ArchivedfileKeys(){
		
	}
	
	public ArchivedfileKeys(String filetype, String fileid){
		super();
		this.filetype = filetype;
		this.fileid = fileid;
	}
	
	/**
	 * 002
	 * @return java.lang.String filetype
	 */
	public java.lang.String getFiletype() {
		return this.filetype;
	}
	
	/** 0001
	 * @param data Set the filetype
	 */	
	public void setFiletype(java.lang.String data) {
		this.filetype = data;    //zzz
	}	
	
	/**
	 * 002
	 * @return java.lang.String fileid
	 */
	public java.lang.String getFileid() {
		return this.fileid;
	}
	
	/** 0001
	 * @param data Set the fileid
	 */	
	public void setFileid(java.lang.String data) {
		this.fileid = data;    //zzz
	}	



}
