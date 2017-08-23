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
public class TurnkeyMessageLogDetailKeys implements java.io.Serializable{


    /**
     * task java.lang.String , PK     * @GeneratedValue(strategy = GenerationType.AUTO)
     */
	@Column(name = "task", nullable = false)
	protected java.lang.String task;
	
	 /**
     * seqno java.lang.String , PK     * @GeneratedValue(strategy = GenerationType.AUTO)
     */
	@Column(name = "seqno", nullable = false)
	protected java.lang.String seqno;
	
	/**
     * subseqno java.lang.String , PK     * @GeneratedValue(strategy = GenerationType.AUTO)
     */
	@Column(name = "subseqno", nullable = false)
	protected java.lang.String subseqno;
	
	
	public TurnkeyMessageLogDetailKeys(){
		
	}
	
	public TurnkeyMessageLogDetailKeys(String task, String seqno, String subseqno){
		super();
		this.task = task;
		this.seqno = seqno;
		this.subseqno = subseqno;
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
	 * @return the seqno
	 */
	public java.lang.String getSeqno() {
		return seqno;
	}

	/**
	 * @param seqno the seqno to set
	 */
	public void setSeqno(java.lang.String seqno) {
		this.seqno = seqno;
	}

	/**
	 * @return the subseqno
	 */
	public java.lang.String getSubseqno() {
		return subseqno;
	}

	/**
	 * @param subseqno the subseqno to set
	 */
	public void setSubseqno(java.lang.String subseqno) {
		this.subseqno = subseqno;
	}


}
