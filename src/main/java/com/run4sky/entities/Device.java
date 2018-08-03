package com.run4sky.entities;

/**
*
* @author user
*/
public class Device {
   
   private int id;
   private String macAddress;
   private String privateIp;
   private String publicIp;
   private boolean registered = false;
   private String name;
   private String status;
   private String type;
   private String description;  
   
   public Device() {
   }
   

   /**
 * @return the id
 */
public int getId() {
	return id;
}


/**
 * @param id the id to set
 */
public void setId(int id) {
	this.id = id;
}


/**
 * @return the macAddress
 */
public String getMacAddress() {
	return macAddress;
}


/**
 * @param macAddress the macAddress to set
 */
public void setMacAddress(String macAddress) {
	this.macAddress = macAddress;
}


/**
 * @return the privateIp
 */
public String getPrivateIp() {
	return privateIp;
}


/**
 * @param privateIp the privateIp to set
 */
public void setPrivateIp(String privateIp) {
	this.privateIp = privateIp;
}


/**
 * @return the publicIp
 */
public String getPublicIp() {
	return publicIp;
}


/**
 * @param publicIp the publicIp to set
 */
public void setPublicIp(String publicIp) {
	this.publicIp = publicIp;
}


/**
 * @return the registered
 */
public boolean isRegistered() {
	return registered;
}


/**
 * @param registered the registered to set
 */
public void setRegistered(boolean registered) {
	this.registered = registered;
}


/**
 * @return the name
 */
public String getName() {
	return name;
}


/**
 * @param name the name to set
 */
public void setName(String name) {
	this.name = name;
}


/**
 * @return the status
 */
public String getStatus() {
	return status;
}


/**
 * @param status the status to set
 */
public void setStatus(String status) {
	this.status = status;
}


/**
 * @return the type
 */
public String getType() {
	return type;
}


/**
 * @param type the type to set
 */
public void setType(String type) {
	this.type = type;
}


/**
 * @return the description
 */
public String getDescription() {
	return description;
}


/**
 * @param description the description to set
 */
public void setDescription(String description) {
	this.description = description;
}




   
}

