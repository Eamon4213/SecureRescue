package com.trustworthy.kerberosapi.Models;

import org.json.JSONException;
import org.json.JSONObject;

import com.trustworthy.kerberosapi.HTTP.KerberosClient;

import android.util.Log;

public class TGSResponse {
	public static final String JSON_KEY_KEYCV = "key_c_service";
	public static final String JSON_KEY_SERVICENAME = "servicename";
	public static final String JSON_KEY_TIMESTAMP = "timestamp";
	public static final String JSON_KEY_STICKET = "ticket";
	public static final String JSON_KEY_ERRORCODE = "errorCode";
	
	public static final int ERROR_AUTHENTICATE_FAILED = 1;
    public static final int ERROR_TIMEOUT = 2;
    public static final int ERROR_NOSUCHSERVICE = 3;
	
	private String key_c_v;
	private String servicename;
	private String timestamp;
	private ServiceTicket ticket;
	private int errorCode;
	private boolean isSealed = true;
	
	public boolean unSeal(String key){
		if(isSealed){
			key_c_v = AESEncryption.aesDecrypt(key_c_v, key);
			servicename = AESEncryption.aesDecrypt(servicename, key);
			timestamp = AESEncryption.aesDecrypt(timestamp, key);
			
			isSealed = false;
			
			if(key_c_v.length() == KerberosClient.SESSION_KEY_LENGTH)
				return true;
			else
				return false;
		}
		
		return false;
	}
		
	public static TGSResponse parseJSONObject(JSONObject poiItem){
		try{
			int errCode = poiItem.getInt(JSON_KEY_ERRORCODE);
			if(errCode != 0)
				return new TGSResponse(errCode);
			
			TGSResponse response = new TGSResponse();
			response.setKey_c_v(poiItem.getString(JSON_KEY_KEYCV));
			response.setServicename(poiItem.getString(JSON_KEY_SERVICENAME));
			response.setTimestamp(poiItem.getString(JSON_KEY_TIMESTAMP));
			
			JSONObject ticketObj = poiItem.getJSONObject(JSON_KEY_STICKET);
			ServiceTicket ticket = ServiceTicket.parceJSONObject(ticketObj);
			response.setTicket(ticket);
			
			return response;
		}catch (JSONException e){
			Log.e("ASResponseHttpGetParseError", e.toString());
			return null;
		}
	}
	
	public TGSResponse(int errorCode){
		this.errorCode = errorCode;
	}
	
	public TGSResponse(){}
	
	public String getKey_c_v() {
		return key_c_v;
	}
	public void setKey_c_v(String key_c_v) {
		this.key_c_v = key_c_v;
	}
	public String getServicename() {
		return servicename;
	}
	public void setServicename(String username) {
		this.servicename = username;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public ServiceTicket getTicket() {
		return ticket;
	}
	public void setTicket(ServiceTicket ticket) {
		this.ticket = ticket;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
