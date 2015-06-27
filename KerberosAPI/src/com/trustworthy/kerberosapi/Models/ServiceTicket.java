package com.trustworthy.kerberosapi.Models;

import java.io.Serializable;

import org.json.JSONObject;

public class ServiceTicket implements Serializable{
	public static final String JSON_KEY_USERNAME = "username";
	public static final String JSON_KEY_SERVICENAME = "servicename";
	public static final String JSON_KEY_MACADDR = "macaddr";
	public static final String JSON_KEY_KEYCV = "key_c_v";
	public static final String JSON_KEY_PRIORITY = "user_priority";
	public static final String JSON_KEY_ISSEALED = "isSealed";
	public static final String JSON_KEY_KEYDATA = "key_data";
	
	private String username;
	private String servicename;
	private String macaddress;
	private String key_c_v;
	private String priority;
	private String key_data;
	private boolean isSealed;
	
	public static ServiceTicket parceJSONObject(JSONObject ticketObj){
		ServiceTicket ticket = new ServiceTicket();
		try{
			ticket.setKey_c_v(ticketObj.getString(ServiceTicket.JSON_KEY_KEYCV));
			ticket.setUsername(ticketObj.getString(ServiceTicket.JSON_KEY_USERNAME));
			ticket.setMacaddress(ticketObj.getString(ServiceTicket.JSON_KEY_MACADDR));
			ticket.setServicename(ticketObj.getString(ServiceTicket.JSON_KEY_SERVICENAME));
			ticket.setPriority(ticketObj.getString(ServiceTicket.JSON_KEY_PRIORITY));
			ticket.setKey_data(ticketObj.getString(ServiceTicket.JSON_KEY_KEYDATA));
			ticket.setSealed(ticketObj.getBoolean(ServiceTicket.JSON_KEY_ISSEALED));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		return ticket;
	}
	
	public JSONObject getJSONObject(){
		JSONObject jsonObj = new JSONObject();
		try{
			jsonObj.put(JSON_KEY_USERNAME, username);
			jsonObj.put(JSON_KEY_SERVICENAME, servicename);
			jsonObj.put(JSON_KEY_MACADDR, macaddress);
			jsonObj.put(JSON_KEY_KEYCV, key_c_v);
			jsonObj.put(JSON_KEY_PRIORITY, priority);
			jsonObj.put(JSON_KEY_ISSEALED, isSealed);
			jsonObj.put(JSON_KEY_KEYDATA, key_data);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return jsonObj;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getServicename() {
		return servicename;
	}
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	public String getMacaddress() {
		return macaddress;
	}
	public void setMacaddress(String macaddress) {
		this.macaddress = macaddress;
	}
	public String getKey_c_v() {
		return key_c_v;
	}
	public void setKey_c_v(String key_c_v) {
		this.key_c_v = key_c_v;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getKey_data() {
		return key_data;
	}

	public void setKey_data(String key_data) {
		this.key_data = key_data;
	}

	public boolean isSealed() {
		return isSealed;
	}
	public void setSealed(boolean isSealed) {
		this.isSealed = isSealed;
	}
	
}
