package com.trustworthy.kerberosapi.Models;

public class TGSTicket {
	public static final String JSON_KEY_USERNAME = "username";
	public static final String JSON_KEY_TGSNAME = "tgsname";
	public static final String JSON_KEY_MACADDR = "macaddress";
	public static final String JSON_KEY_KEY_CTGS = "key_c_tgs";
	public static final String JSON_KEY_ISSEALED = "isEncrypted";
	public static final String JSON_KEY_PRIORITY = "user_priority";
	
	private String userName;
	private String tgsName;
	private String macAddr;
	private String key_C_TGS;
	private String priority;
	private boolean isSealed;
	
	public TGSTicket(){
		
	}
	
	public TGSTicket(String username, String tgsname, String macAddr, String key_ctgs, String priority, boolean isSealed){
		this.userName = username;
		this.tgsName = tgsname;
		this.macAddr = macAddr;
		this.key_C_TGS = key_ctgs;
		this.priority = priority;
		this.isSealed = isSealed;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTgsName() {
		return tgsName;
	}
	public void setTgsName(String tgsName) {
		this.tgsName = tgsName;
	}
	public String getMacAddr() {
		return macAddr;
	}
	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}
	public String getKey_C_TGS() {
		return key_C_TGS;
	}
	public void setKey_C_TGS(String key_C_TGS) {
		this.key_C_TGS = key_C_TGS;
	}

	public boolean isSealed() {
		return isSealed;
	}

	public void setSealed(boolean isSealed) {
		this.isSealed = isSealed;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
	
}
