package com.trustworthy.kerberosapi.Models;

import org.json.JSONObject;

public class ServiceRequest {
	public static final String JSON_KEY_TICKET = "ticket";
	public static final String JSON_KEY_AUTHENTICATOR = "authenticator";
	public static final String JSON_KEY_EXTRAREQUEST= "extraRequest";
	public static final String JSON_KEY_POIS= "pois";
	public static final String JSON_KEY_MSGS = "msgs";
	public static final int REQUEST_CREATEPOI = 1;
	public static final int REQUEST_CREATEMSG = 2;
	
	private ServiceTicket ticket;
	private AuthenticatorC authen;
	private int extraRequest=0;
	
	private IncidentPOIs pois;
	private IncidentMSGs msgs;
	
	private JSONObject json;
	
	public ServiceRequest(ServiceTicket ticket, AuthenticatorC authen){
		this.ticket = ticket;
		this.authen = authen;
		
		json = new JSONObject();
	}
	
	public ServiceRequest(ServiceTicket ticket, AuthenticatorC authen, IncidentPOIs pois){
		this.ticket = ticket;
		this.authen = authen;
		this.pois = pois;
		
		extraRequest = REQUEST_CREATEPOI;
		
		json = new JSONObject();
	}
	
	public ServiceRequest(ServiceTicket ticket, AuthenticatorC authen, IncidentMSGs msgs){
		this.ticket = ticket;
		this.authen = authen;
		this.msgs = msgs;
		
		extraRequest = REQUEST_CREATEMSG;
		
		json = new JSONObject();
	}
	
	public JSONObject getPutJSON(){
		try{
			if(ticket!=null)
				json.put(JSON_KEY_TICKET, ticket.getJSONObject());
			
			json.put(JSON_KEY_AUTHENTICATOR, authen.getJSONObject());
			json.put(JSON_KEY_EXTRAREQUEST, extraRequest);
			
			if(pois!=null)
				json.put(JSON_KEY_POIS, pois.getPutJSON());
			
			if(msgs!=null)
				json.put(JSON_KEY_MSGS, msgs.getPutJSON());
			
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return json;
	}

	public int getExtraRequest() {
		return extraRequest;
	}

	public void setExtraRequest(int extraRequest) {
		this.extraRequest = extraRequest;
	}
	
}
