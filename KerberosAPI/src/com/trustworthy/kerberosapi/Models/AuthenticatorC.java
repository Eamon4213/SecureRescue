package com.trustworthy.kerberosapi.Models;

import java.io.Serializable;

import org.json.JSONObject;

public class AuthenticatorC implements Serializable{
	public static final String JSON_KEY_USERNAME = "username";
	public static final String JSON_KEY_MACADDR = "macaddr";
	public static final String JSON_KEY_TIMESTAMP = "timestamp";
	public static final String JSON_KEY_ISENCRYPTED = "isEncrypted";

	private String username;
	private String macaddr;
	private String timestamp;
	private boolean isSealed = false;
	
	public AuthenticatorC(){};
	public AuthenticatorC(String username, String macaddr, String timestamp){
		this.username = username;
		this.macaddr = macaddr;
		this.timestamp = timestamp;
	}
	
	public JSONObject getJSONObject(){
		JSONObject json = new JSONObject();
		try{
			json.put(JSON_KEY_USERNAME, username);
			json.put(JSON_KEY_MACADDR, macaddr);
			json.put(JSON_KEY_TIMESTAMP, timestamp);
			json.put(JSON_KEY_ISENCRYPTED, isSealed);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		return json;
	}
	public void seal(String key){
		if(!isSealed){
			username = AESEncryption.aesEncrypt(username, key);
			macaddr = AESEncryption.aesEncrypt(macaddr, key);
			timestamp = AESEncryption.aesEncrypt(timestamp, key);
			
			isSealed = true;
		}
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMacaddr() {
		return macaddr;
	}
	public void setMacaddr(String macaddr) {
		this.macaddr = macaddr;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public boolean isSealed() {
		return isSealed;
	}
	public void setSealed(boolean isSealed) {
		this.isSealed = isSealed;
	}
	
	
}
