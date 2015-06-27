package com.trustworthy.kerberosapi.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class IncidentPOIs implements Serializable{
	public static final String JSON_KEY_ID = "str_id";
	public static final String JSON_KEY_POITYPE = "str_POIType";
	public static final String JSON_KEY_POINAME = "POIName";
	public static final String JSON_KEY_LOCATION = "POILocation";
	public static final String JSON_KEY_SECRETLEVEL = "str_SecretLevel";
	public static final String JSON_KEY_ISENCRYPTED = "isEncrypted";
	
	public static final int POITYPE_VICTIM = 1;
	public static final int POITYPE_RESPONDER = 2;
	public static final int POITYPE_COMMANDER = 3;
	public static final int POITYPE_LANDMARKS = 0;
	
	private int id;
	private int type;
	private String name;
	private String location;
	private int secretLevel;
	private boolean isEncrypted;
	
	private String str_type;
	private String str_id;
	private String str_secretLevel;
	
	private JSONObject json;
	
	public IncidentPOIs(){}
	
	public IncidentPOIs(int poiType, String name, String location, int secretLevel){
		this.type = poiType;
		this.name = name;
		this.location = location;
		this.secretLevel = secretLevel;
		
		this.str_type = Integer.toString(poiType);
		this.str_secretLevel = Integer.toString(secretLevel);
		this.isEncrypted = false;
		
		json = new JSONObject();
	}
	
	public JSONObject getPutJSON(){
		try{
			json.put(JSON_KEY_POINAME, name);
			json.put(JSON_KEY_LOCATION, location);
			json.put(JSON_KEY_POITYPE, str_type);
			json.put(JSON_KEY_SECRETLEVEL, str_secretLevel);
			json.put(JSON_KEY_ISENCRYPTED, isEncrypted);
		}catch(Exception e){
			e.printStackTrace();
			Log.e("Error in HTTP Er Update Post", e.toString());
		}
		
		return json;		
	}
	
	public static List<IncidentPOIs> parceJSONArray(JSONArray jsonArray){
		List<IncidentPOIs> pois = new ArrayList<IncidentPOIs>();
		for(int i=0; i<jsonArray.length(); i++){
			try{
				JSONObject poiItem;
				IncidentPOIs poi = new IncidentPOIs();
				
				poiItem = jsonArray.getJSONObject(i);
				poi.setStr_id(poiItem.getString(JSON_KEY_ID));
				poi.setLocation(poiItem.getString(JSON_KEY_LOCATION));
				poi.setName(poiItem.getString(JSON_KEY_POINAME));
				poi.setStr_secretLevel(poiItem.getString(JSON_KEY_SECRETLEVEL));
				poi.setStr_type(poiItem.getString(JSON_KEY_POITYPE));
				poi.setEncrypted(poiItem.getBoolean(JSON_KEY_ISENCRYPTED));
				
				if(poi.isEncrypted == false){
					poi.setId(Integer.valueOf(poi.getStr_id()));
					poi.setSecretLevel(Integer.valueOf(poi.getSecretLevel()));
					poi.setType(Integer.valueOf(poi.getType()));
				}
				
				pois.add(poi);
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		return pois;
	}
	
	public boolean decrypt(String key){
		if(isEncrypted == false)
			return true;
		
		try{
			id = Integer.valueOf(AESEncryption.aesDecrypt(str_id, key));
			type = Integer.valueOf(AESEncryption.aesDecrypt(str_type, key));
			secretLevel = Integer.valueOf(AESEncryption.aesDecrypt(str_secretLevel, key));
			name = AESEncryption.aesDecrypt(name, key);
			location = AESEncryption.aesDecrypt(location, key);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean encrypt(String key){
		if(isEncrypted == true)
			return true;
		
		try{
			str_type = AESEncryption.aesEncrypt(Integer.toString(type), key);
			str_secretLevel = AESEncryption.aesEncrypt(Integer.toString(secretLevel), key);
			name = AESEncryption.aesEncrypt(name, key);
			location = AESEncryption.aesEncrypt(location, key);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getSecretLevel() {
		return secretLevel;
	}
	public void setSecretLevel(int secretLevel) {
		this.secretLevel = secretLevel;
	}

	public String getStr_id() {
		return str_id;
	}

	public void setStr_id(String str_id) {
		this.str_id = str_id;
	}

	public String getStr_type() {
		return str_type;
	}

	public void setStr_type(String str_type) {
		this.str_type = str_type;
	}

	public String getStr_secretLevel() {
		return str_secretLevel;
	}

	public void setStr_secretLevel(String str_secretLevel) {
		this.str_secretLevel = str_secretLevel;
	}

	public boolean isEncrypted() {
		return isEncrypted;
	}

	public void setEncrypted(boolean isEncrypted) {
		this.isEncrypted = isEncrypted;
	}
	
	
}
