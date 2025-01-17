package com.run4sky.beans;
// Generated Sep 1, 2018, 8:04:03 PM by Hibernate Tools 5.2.11.Final

/**
 * Client generated by hbm2java
 */
public class Client implements java.io.Serializable {

	private int id;
	private String name;
	private String description;
	private boolean isRegistered;

	public Client() {
	}

	public Client(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Client(int id, String name, String description, boolean isRegistered) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.isRegistered = isRegistered;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getIsRegistered() {
		return this.isRegistered;
	}

	public void setIsRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

}
