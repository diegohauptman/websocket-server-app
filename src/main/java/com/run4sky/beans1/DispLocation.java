package com.run4sky.beans1;
// Generated Sep 8, 2018, 3:45:43 PM by Hibernate Tools 5.2.11.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DispLocation generated by hbm2java
 */
@Entity
@Table(name = "DispLocation", catalog = "Ximds_EvilCorp")
public class DispLocation implements java.io.Serializable {

	private int id;
	private int dispId;
	private String externalIp;
	private String internalIp;

	public DispLocation() {
	}

	public DispLocation(int id, int dispId, String externalIp) {
		this.id = id;
		this.dispId = dispId;
		this.externalIp = externalIp;
	}

	public DispLocation(int id, int dispId, String externalIp, String internalIp) {
		this.id = id;
		this.dispId = dispId;
		this.externalIp = externalIp;
		this.internalIp = internalIp;
	}

	@Id

	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "DispID", nullable = false)
	public int getDispId() {
		return this.dispId;
	}

	public void setDispId(int dispId) {
		this.dispId = dispId;
	}

	@Column(name = "externalIP", nullable = false, length = 300)
	public String getExternalIp() {
		return this.externalIp;
	}

	public void setExternalIp(String externalIp) {
		this.externalIp = externalIp;
	}

	@Column(name = "internalIP", length = 50)
	public String getInternalIp() {
		return this.internalIp;
	}

	public void setInternalIp(String internalIp) {
		this.internalIp = internalIp;
	}

}
