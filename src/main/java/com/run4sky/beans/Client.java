package com.run4sky.beans;
// Generated 03-ago-2018 14:17:12 by Hibernate Tools 5.2.11.Final

/**
 * Client generated by hbm2java
 */
public class Client implements java.io.Serializable {

	private int id;
	private String name;
	private String description;

	public Client() {
	}

	public Client(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
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

}
