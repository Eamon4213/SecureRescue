package com.trustworthy.kerberosapi.Models;

import org.json.JSONObject;

import android.util.Log;

public class UserInfo {
	private static final String JSON_KEY_USERNAME = "Username";
	private static final String JSON_KEY_PASSWROD = "Password";
	private static final String JSON_KEY_AUTHORITY = "Authority";
	private static final String JSON_KEY_TIMESTAMP = "C_Time";
	private static final String JSON_KEY_DETAIL = "Detail";
	
	private String username;
	private String password;
	private String detail;
	private String timestamp;
	private int priority;
	
	private JSONObject json;
	
	public UserInfo(String username, String password, String detail, String timestamp){
		this.username = username;
		this.password = password;
		this.detail = detail;
		this.timestamp = timestamp;
		this.priority = 0;
		
		json = new JSONObject();
	}
	
	public JSONObject getPutJSON(){
		try{
			json.put(JSON_KEY_USERNAME, username);
			json.put(JSON_KEY_PASSWROD, password);
			json.put(JSON_KEY_AUTHORITY, priority);
			json.put(JSON_KEY_TIMESTAMP, timestamp);
			json.put(JSON_KEY_DETAIL, detail);
		}catch(Exception e){
			e.printStackTrace();
			Log.e("Error in HTTP Er Update Post", e.toString());
		}
		
		return json;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
}
