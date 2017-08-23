package com.meshinnovation.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * <p>Base Object for persistence.</p>
 *
 * <p>If you extends this BaseObject, you should create three columns in your table:</p>
 * <ul>
 * <li>date_created</li>
 * <li>last_updated</li>
 * <li>enabled</li>
 * </ul>
 *
 * <p>The attribute <code>dateCreated</code> will be updated before persisting.</p>
 * <p>The attribute <code>lastUpdated</code> will be updated before updating.</p>
 *
 *
 * @author Reder Tseng<reder@meshinnovation.com>, Jessie Wang<jessiewang@meshinnovation.com>
 */
@MappedSuperclass
public abstract class JpaBaseObject extends com.meshinnovation.db.model.BaseObject {

	private static final long serialVersionUID = 1L;

	@Column(name="date_created", nullable = false)
	protected Date dateCreated;

	@Column(name="last_updated")
	protected Date lastUpdated;

	@Column(name="enabled", nullable = false)
	protected boolean enabled = true;

	@SuppressWarnings("unused")
	@PrePersist
	private void prePersist(){
		this.dateCreated = new Date();
	}

	@SuppressWarnings("unused")
	@PreUpdate
	private void preUpdate(){
		this.lastUpdated = new Date();
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}


}
