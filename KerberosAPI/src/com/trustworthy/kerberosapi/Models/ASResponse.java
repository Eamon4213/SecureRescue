package com.trustworthy.kerberosapi.Models;

import org.json.JSONException;
import org.json.JSONObject;

import com.trustworthy.kerberosapi.HTTP.KerberosClient;

import android.util.Log;

public class ASResponse {
	private static final String JSON_KEY_KEY_CTGS = "key_c_tgs";
	private static final String JSON_KEY_TIMESTAMP = "timestamp";
	private static final String JSON_KEY_LIFETIME = "lifetime";
	private static final String JSON_KEY_AUTHORITY = "authority";
	private static final String JSON_KEY_TICKET = "ticket";
	private static final String JSON_KEY_ERRORCODE = "errorCode";
	
	public static final int ERRORCODE_INPUTVALID = 1;
	public static final int ERRORCODE_USERNOTFOUND = 2;
	public static final int ERRORCODE_KEYNOTFOUND = 3;

	private String key_C_TGS;
	private String timestamp;
	private String lifetime;
	private int authority;
	private String str_authority;
	private TGSTicket ticket;
	private int errorCode;
	private boolean isSealed = true;
	
	public static ASResponse parseJSONObject(JSONObject poiItem){
		try{
			int errCode = poiItem.getInt(ASResponse.JSON_KEY_ERRORCODE);
			if(errCode != 0)                        //Error
				return new ASResponse(errCode);
			
			ASResponse response = new ASResponse();
			response.setKey_C_TGS(poiItem.getString(ASResponse.JSON_KEY_KEY_CTGS));
			response.setLifetime(poiItem.getString(ASResponse.JSON_KEY_LIFETIME));
			response.setStr_authority(poiItem.getString(ASResponse.JSON_KEY_AUTHORITY));
			response.setErrorCode(poiItem.getInt(ASResponse.JSON_KEY_ERRORCODE));
			response.setTimestamp(poiItem.getString(ASResponse.JSON_KEY_TIMESTAMP));
			
			TGSTicket ticket = new TGSTicket();
			JSONObject ticketObj = poiItem.getJSONObject(JSON_KEY_TICKET);
			ticket.setUserName(ticketObj.getString(TGSTicket.JSON_KEY_USERNAME));
			ticket.setTgsName(ticketObj.getString(TGSTicket.JSON_KEY_TGSNAME));
			ticket.setMacAddr(ticketObj.getString(TGSTicket.JSON_KEY_MACADDR));
			ticket.setKey_C_TGS(ticketObj.getString(TGSTicket.JSON_KEY_KEY_CTGS));
			ticket.setPriority(ticketObj.getString(TGSTicket.JSON_KEY_PRIORITY));
			ticket.setSealed(ticketObj.getBoolean(TGSTicket.JSON_KEY_ISSEALED));
			response.setTicket(ticket);
			
			return response;
		}catch (JSONException e){
			Log.e("ASResponseHttpGetParseError", e.toString());
			return null;
		}
	}
	
	public ASResponse (){}
	
	public ASResponse (int errorCode){
		this.errorCode = errorCode;
	}
	
	public boolean unSeal(String key){        //decrypt the information using the key
		if(isSealed){
			key_C_TGS = AESEncryption.aesDecrypt(key_C_TGS, key);
			timestamp = AESEncryption.aesDecrypt(timestamp, key);
			lifetime = AESEncryption.aesDecrypt(lifetime, key);
			try{
				authority = Integer.valueOf(AESEncryption.aesDecrypt(str_authority, key));
			}catch(Exception e){
				return false;
			}
			

			isSealed = false;
			
			if(key_C_TGS.length() == KerberosClient.SESSION_KEY_LENGTH)
				return true;
			else
				return false;
		}
		return false;
	}
	public String getKey_C_TGS() {
		return key_C_TGS;
	}
	public void setKey_C_TGS(String key_C_TGS) {
		this.key_C_TGS = key_C_TGS;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getLifetime() {
		return lifetime;
	}
	public void setLifetime(String lifetime) {
		this.lifetime = lifetime;
	}
	public TGSTicket getTicket() {
		return ticket;
	}
	public void setTicket(TGSTicket ticket) {
		this.ticket = ticket;
	}
	public int getAuthority() {
		return authority;
	}

	public void setAuthority(int authority) {
		this.authority = authority;
	}

	public void setStr_authority(String str_authority) {
		this.str_authority = str_authority;
	}

	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
