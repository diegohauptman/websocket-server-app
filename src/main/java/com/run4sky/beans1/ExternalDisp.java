package com.run4sky.beans1;
// Generated Sep 8, 2018, 3:45:43 PM by Hibernate Tools 5.2.11.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ExternalDisp generated by hbm2java
 */
@Entity
@Table(name = "ExternalDisp", catalog = "Ximds_EvilCorp")
public class ExternalDisp implements java.io.Serializable {

	private int id;
	private String mac;
	private String cpu;
	private String ram;
	private String so;
	private int ncpu;
	private String isRegistered;

	public ExternalDisp() {
	}

	public ExternalDisp(int id, String mac, String cpu, String ram, String so, int ncpu) {
		this.id = id;
		this.mac = mac;
		this.cpu = cpu;
		this.ram = ram;
		this.so = so;
		this.ncpu = ncpu;
	}

	public ExternalDisp(int id, String mac, String cpu, String ram, String so, int ncpu, String isRegistered) {
		this.id = id;
		this.mac = mac;
		this.cpu = cpu;
		this.ram = ram;
		this.so = so;
		this.ncpu = ncpu;
		this.isRegistered = isRegistered;
	}

	@Id

	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "MAC", nullable = false, length = 300)
	public String getMac() {
		return this.mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	@Column(name = "CPU", nullable = false, length = 300)
	public String getCpu() {
		return this.cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	@Column(name = "RAM", nullable = false, length = 300)
	public String getRam() {
		return this.ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	@Column(name = "SO", nullable = false, length = 300)
	public String getSo() {
		return this.so;
	}

	public void setSo(String so) {
		this.so = so;
	}

	@Column(name = "NCPU", nullable = false)
	public int getNcpu() {
		return this.ncpu;
	}

	public void setNcpu(int ncpu) {
		this.ncpu = ncpu;
	}

	@Column(name = "isRegistered", length = 300)
	public String getIsRegistered() {
		return this.isRegistered;
	}

	public void setIsRegistered(String isRegistered) {
		this.isRegistered = isRegistered;
	}

}
