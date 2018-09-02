package com.run4sky.beans;
// Generated Sep 1, 2018, 8:04:03 PM by Hibernate Tools 5.2.11.Final

/**
 * ClienService generated by hbm2java
 */
public class ClienService implements java.io.Serializable {

	private int id;
	private String mac;
	private String cpu;
	private String ram;
	private String so;
	private int ncpu;
	private boolean isRegistered;

	public ClienService() {
	}

	public ClienService(int id, String mac, String cpu, String ram, String so, int ncpu) {
		this.id = id;
		this.mac = mac;
		this.cpu = cpu;
		this.ram = ram;
		this.so = so;
		this.ncpu = ncpu;
	}

	public ClienService(int id, String mac, String cpu, String ram, String so, int ncpu, boolean isRegistered) {
		this.id = id;
		this.mac = mac;
		this.cpu = cpu;
		this.ram = ram;
		this.so = so;
		this.ncpu = ncpu;
		this.isRegistered = isRegistered;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMac() {
		return this.mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getCpu() {
		return this.cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getRam() {
		return this.ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	public String getSo() {
		return this.so;
	}

	public void setSo(String so) {
		this.so = so;
	}

	public int getNcpu() {
		return this.ncpu;
	}

	public void setNcpu(int ncpu) {
		this.ncpu = ncpu;
	}

	public boolean getIsRegistered() {
		return this.isRegistered;
	}

	public void setIsRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

}
