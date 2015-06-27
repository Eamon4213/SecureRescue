package com.trustworthy.kerberosapi.Models;

import org.json.JSONObject;

import android.util.Log;

public class TGSRequest {
	public static final String JSON_KEY_SERVICENAME = "serviceName";
	public static final String JSON_KEY_TGSTICKET = "ticket";
	public static final String JSON_KEY_AUTHENTICATOR = "authenticator";
	
	private String serviceName;
	private TGSTicket ticket;
	private AuthenticatorC authenticator;
	
	private JSONObject json;
	
	public TGSRequest(String serviceName, TGSTicket ticket, AuthenticatorC authenticator){
		this.serviceName = serviceName;
		this.ticket = ticket;
		this.authenticator = authenticator;
		
		json = new JSONObject();
	}
	
	public JSONObject getPutJSON(){
		try{
			json.put(JSON_KEY_SERVICENAME, serviceName);
			
			JSONObject json_Ticket = new JSONObject();
			json_Ticket.put(TGSTicket.JSON_KEY_USERNAME, ticket.getUserName());
			json_Ticket.put(TGSTicket.JSON_KEY_TGSNAME, ticket.getTgsName());
			json_Ticket.put(TGSTicket.JSON_KEY_MACADDR, ticket.getMacAddr());
			json_Ticket.put(TGSTicket.JSON_KEY_KEY_CTGS, ticket.getKey_C_TGS());
			json_Ticket.put(TGSTicket.JSON_KEY_ISSEALED, ticket.isSealed());
			json_Ticket.put(TGSTicket.JSON_KEY_PRIORITY, ticket.getPriority());
			
			json.put(JSON_KEY_TGSTICKET, json_Ticket);
			
			JSONObject json_Auth = new JSONObject();
			json_Auth.put(AuthenticatorC.JSON_KEY_USERNAME, authenticator.getUsername());
			json_Auth.put(AuthenticatorC.JSON_KEY_MACADDR, authenticator.getMacaddr());
			json_Auth.put(AuthenticatorC.JSON_KEY_TIMESTAMP, authenticator.getTimestamp());
			json_Auth.put(AuthenticatorC.JSON_KEY_ISENCRYPTED, authenticator.isSealed());
			
			json.put(JSON_KEY_AUTHENTICATOR, json_Auth);
			
		}catch(Exception e){
			e.printStackTrace();
			Log.e("Error in HTTP Er Update Post", e.toString());
		}
		
		return json;
	}
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public TGSTicket getTicket() {
		return ticket;
	}
	public void setTicket(TGSTicket ticket) {
		this.ticket = ticket;
	}
	public AuthenticatorC getAuthenticator() {
		return authenticator;
	}
	public void setAuthenticator(AuthenticatorC authenticator) {
		this.authenticator = authenticator;
	}
}
