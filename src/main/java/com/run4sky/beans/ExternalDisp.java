package com.run4sky.beans;
// Generated 03-ago-2018 14:17:12 by Hibernate Tools 5.2.11.Final

/**
 * ExternalDisp generated by hbm2java
 */
public class ExternalDisp implements java.io.Serializable {

	private int id;
	private String mac;
	private String cpu;
	private String ram;
	private String so;
	private int ncpu;

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

	public int getncpu() {
		return this.ncpu;
	}

	public void setncpu(int ncpu) {
		this.ncpu = ncpu;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExternalDisp [id=" + id + ", mac=" + mac + ", cpu=" + cpu + ", ram=" + ram + ", so=" + so + ", ncpu="
				+ ncpu + "]";
	}
	
	
}
