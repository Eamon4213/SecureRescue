package com.trustworthy.kerberosapi.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class Incident implements Serializable{

	public static final String JSON_KEY_ID = "id";
	public static final String JSON_KEY_NAME = "Name";
	public static final String JSON_KEY_DETAIL = "Detail";
	public static final String JSON_KEY_SECURITYLEVEL = "SecurityLevel";
	
	private int incidentId;
	private String incidentName;
	private String incidentDetail;
	private int securityLevel;
	
	private JSONObject json;
	
	public Incident(){}
	
	public Incident(String incidentName, String incidentDetail, int securityLevel){
		this.incidentName = incidentName;
		this.incidentDetail = incidentDetail;
		this.securityLevel = securityLevel;
		
		json = new JSONObject();
	}
	
	public JSONObject getPutJSON(){
		try{
			json.put(JSON_KEY_NAME, incidentName);
			json.put(JSON_KEY_DETAIL, incidentDetail);
			json.put(JSON_KEY_SECURITYLEVEL, securityLevel);
		}catch(Exception e){
			e.printStackTrace();
			Log.e("Error in HTTP Er Update Post", e.toString());
		}
		
		return json;
	}
	
	public static List<Incident> parceJSONArray(JSONArray jsonArray){
		List<Incident> incidents = new ArrayList<Incident>();
		for(int i=0; i<jsonArray.length(); i++){
			try{
				JSONObject poiItem;
				Incident incident = new Incident();
				
				poiItem = jsonArray.getJSONObject(i);
				incident.setIncidentId(poiItem.getInt(JSON_KEY_ID));
				incident.setIncidentName(poiItem.getString(JSON_KEY_NAME));
				incident.setIncidentDetail(poiItem.getString(JSON_KEY_DETAIL));
				incident.setSecurityLevel(poiItem.getInt(JSON_KEY_SECURITYLEVEL));
				
				incidents.add(incident);
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		
		return incidents;
	}
	
	public int getIncidentId() {
		return incidentId;
	}
	public void setIncidentId(int incidentId) {
		this.incidentId = incidentId;
	}
	public String getIncidentName() {
		return incidentName;
	}
	public void setIncidentName(String incidentName) {
		this.incidentName = incidentName;
	}
	public String getIncidentDetail() {
		return incidentDetail;
	}
	public void setIncidentDetail(String incidentDetail) {
		this.incidentDetail = incidentDetail;
	}
	public int getSecurityLevel() {
		return securityLevel;
	}
	public void setSecurityLevel(int securityLevel) {
		this.securityLevel = securityLevel;
	}
	
	
}
