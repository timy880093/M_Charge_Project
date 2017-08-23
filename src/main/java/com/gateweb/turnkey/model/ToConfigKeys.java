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
public class ToConfigKeys implements java.io.Serializable{


	/**
     * party_id java.lang.String , PK     * @GeneratedValue(strategy = GenerationType.AUTO)
     */
	@Column(name = "party_id", nullable = false)
	protected java.lang.String partyId;
	
	 /**
     * from_party_id java.lang.String , PK     * @GeneratedValue(strategy = GenerationType.AUTO)
     */
	@Column(name = "from_party_id", nullable = false)
	protected java.lang.String fromPartyId;
	
	public ToConfigKeys(){
		
	}
	
	public ToConfigKeys(String partyId, String fromPartyId){
		this.partyId = partyId;
		this.fromPartyId = fromPartyId;
	}

	/**
	 * @return the partyId
	 */
	public java.lang.String getPartyId() {
		return partyId;
	}

	/**
	 * @param partyId the partyId to set
	 */
	public void setPartyId(java.lang.String partyId) {
		this.partyId = partyId;
	}

	/**
	 * @return the fromPartyId
	 */
	public java.lang.String getFromPartyId() {
		return fromPartyId;
	}

	/**
	 * @param fromPartyId the fromPartyId to set
	 */
	public void setFromPartyId(java.lang.String fromPartyId) {
		this.fromPartyId = fromPartyId;
	}
	

}
