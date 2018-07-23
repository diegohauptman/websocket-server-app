package com.run4sky.entities;

/**
*
* @author user
*/
public class Device {
   
   private int id;
   private String macAddress;
   private String ip;
   private boolean registered = false;
   private String name;
   private String status;
   private String type;
   private String description;
   

   public Device() {
   }

   public String getMacAddress() {
       return macAddress;
   }

   public void setMacAddress(String macAddress) {
       this.macAddress = macAddress;
   }

   public String getIp() {
       return ip;
   }

   public void setIp(String ip) {
       this.ip = ip;
   }

   public boolean isRegistered() {
       return registered;
   }

   public void setRegistered(boolean registered) {
       this.registered = registered;
   }
   
   
   
   public int getId() {
       return id;
   }
   
   public String getName() {
       return name;
   }

   public String getStatus() {
       return status;
   }

   public String getType() {
       return type;
   }
   
   public String getDescription() {
       return description;
   }

   public void setId(int id) {
       this.id = id;
   }
   
   public void setName(String name) {
       this.name = name;
   }

   public void setStatus(String status) {
       this.status = status;
   }

   public void setType(String type) {
       this.type = type;
   }
   
   public void setDescription(String description) {
       this.description = description;
   }
}

