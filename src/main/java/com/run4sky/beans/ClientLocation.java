package com.run4sky.beans;
// Generated 14-ago-2018 13:52:35 by Hibernate Tools 5.2.11.Final

/**
 * ClientLocation generated by hbm2java
 */
public class ClientLocation implements java.io.Serializable {

	private int id;
	private String clientIpint;
	private String clientIpext;
	private int clientId;

	public ClientLocation() {
	}

	public ClientLocation(int id, String clientIpint, String clientIpext, int clientId) {
		this.id = id;
		this.clientIpint = clientIpint;
		this.clientIpext = clientIpext;
		this.clientId = clientId;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClientIpint() {
		return this.clientIpint;
	}

	public void setClientIpint(String clientIpint) {
		this.clientIpint = clientIpint;
	}

	public String getClientIpext() {
		return this.clientIpext;
	}

	public void setClientIpext(String clientIpext) {
		this.clientIpext = clientIpext;
	}

	public int getClientId() {
		return this.clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

}
