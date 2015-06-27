package com.trustworthy.kerberosapi.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class IncidentMSGs implements Serializable{
	public static final String JSON_KEY_ID = "str_id";
	public static final String JSON_KEY_TITLE = "title";
	public static final String JSON_KEY_MESSAGE = "message";
	public static final String JSON_KEY_SECRETLEVEL = "str_secretLevel";
	public static final String JSON_KEY_ISENCRYPTED = "isEncrypted";
	
	private int id;
	private String title;
	private String message;
	private int secretLevel;
	private boolean isEncrypted;
	
	private String str_id;
	private String str_secretLevel;
	
	private JSONObject json;
	
	public IncidentMSGs(){}
	
	public IncidentMSGs(String title, String message, int secretLevel) {
		this.title = title;
		this.message = message;
		this.secretLevel = secretLevel;
		
		this.str_secretLevel = Integer.toString(secretLevel);
		isEncrypted = false;
		
		json = new JSONObject();
	}

	public JSONObject getPutJSON(){
		try{
			json.put(JSON_KEY_TITLE, title);
			json.put(JSON_KEY_MESSAGE, message);
			json.put(JSON_KEY_SECRETLEVEL, str_secretLevel);
			json.put(JSON_KEY_ISENCRYPTED, isEncrypted);
		}catch(Exception e){
			e.printStackTrace();
			Log.e("Error in HTTP Er Update Post", e.toString());
		}
		
		return json;
	}

	public static List<IncidentMSGs> parceJSONArray(JSONArray jsonArray){
		List<IncidentMSGs> msgs = new ArrayList<IncidentMSGs>();
		for(int i=0; i<jsonArray.length(); i++){
			try{
				JSONObject poiItem;
				IncidentMSGs msg = new IncidentMSGs();
				
				poiItem = jsonArray.getJSONObject(i);
				msg.setStr_id(poiItem.getString(JSON_KEY_ID));
				msg.setTitle(poiItem.getString(JSON_KEY_TITLE));
				msg.setMessage(poiItem.getString(JSON_KEY_MESSAGE));
				msg.setStr_secretLevel(poiItem.getString(JSON_KEY_SECRETLEVEL));
				msg.setEncrypted(poiItem.getBoolean(JSON_KEY_ISENCRYPTED));
				
				if(msg.isEncrypted==false){
					msg.setId(Integer.valueOf(msg.getStr_id()));
					msg.setSecretLevel(Integer.valueOf(msg.getSecretLevel()));
				}
				
				msgs.add(msg);
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		return msgs;
	}
	
	public boolean decrypt(String key){
		if(isEncrypted == false)
			return true;
		
		try{
			id = Integer.valueOf(AESEncryption.aesDecrypt(str_id, key));
			secretLevel = Integer.valueOf(AESEncryption.aesDecrypt(str_secretLevel, key));
			title = AESEncryption.aesDecrypt(title, key);
			message = AESEncryption.aesDecrypt(message, key);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean encrypt(String key){
		if(isEncrypted == true)
			return true;
		
		try{
			str_secretLevel = AESEncryption.aesEncrypt(Integer.toString(secretLevel), key);
			title = AESEncryption.aesEncrypt(title, key);
			message = AESEncryption.aesEncrypt(message, key);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
