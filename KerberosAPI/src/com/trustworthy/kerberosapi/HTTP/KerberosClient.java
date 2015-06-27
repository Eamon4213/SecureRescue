package com.trustworthy.kerberosapi.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.trustworthy.kerberosapi.Models.ASResponse;
import com.trustworthy.kerberosapi.Models.AuthenticatorC;
import com.trustworthy.kerberosapi.Models.Incident;
import com.trustworthy.kerberosapi.Models.IncidentMSGs;
import com.trustworthy.kerberosapi.Models.IncidentPOIs;
import com.trustworthy.kerberosapi.Models.ServiceRequest;
import com.trustworthy.kerberosapi.Models.ServiceTicket;
import com.trustworthy.kerberosapi.Models.TGSRequest;
import com.trustworthy.kerberosapi.Models.TGSResponse;
import com.trustworthy.kerberosapi.Models.TGSTicket;
import com.trustworthy.kerberosapi.Models.UserInfo;

import android.text.format.Time;
import android.util.Log;

public class KerberosClient {
	
	private String URL_PRE = "http://s320880.vicp.net:8080/api/";
	//private String URL_PRE = "http://24.91.28.7:8080/api/";
	
	private final String URL_AUTH_SERVER = "Auth";
	private final String URL_TG_SERVER = "TGS";
	private final String URL_SERVICE_1 = "Service1";
	private final String URL_SERVICE_2 = "Service2";
	
	public final int HTTP_RESPONSE_CODE_CREATED = 201;
	public final int HTTP_RESPONSE_CODE_OK = 200;
	public final int HTTP_RESPONSE_CODE_CONFLICT = 409;
	public final int HTTP_RESPONSE_CODE_BADREQUEST = 400;
	
	public final static int SESSION_KEY_LENGTH = 32;
	
	public KerberosClient(){}
	
	public KerberosClient(String url){
		this.URL_PRE = url;
	}
	
	/*****************************************************************************/
	/***********************  Public useful Functions  ***************************/
	/*****************************************************************************/
	
	public static String getTimeStamp(){       //Generate current timestamp
		Time dtNow = new Time();
		dtNow.setToNow();
		String year = ((Integer)(dtNow.year)).toString();
		String month = dtNow.toString().substring(4, 6);
		String day = ((Integer)(dtNow.monthDay)).toString();
		String hour = ((Integer)(dtNow.hour)).toString();
		String minute = ((Integer)(dtNow.minute)).toString();
		String second = ((Integer)(dtNow.second)).toString();
		
		if(month.length()<2){
			month = "0" + month;
		}
		if(day.length()<2){
			day = "0" + day;
		}
		if(hour.length()<2){
			hour = "0" + hour;
		}
		if(minute.length()<2){
			minute = "0" + minute;
		}
		if(second.length()<2){
			second = "0" + second;
		}
		
		String timeStamp = year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second;
	
		return timeStamp;
	}
	
	public ASResponse getTGSTicketFromServer(String username, String tgsname, String macaddress){       //get a TGS ticket from AU server
		String url = URL_PRE + URL_AUTH_SERVER + "?username=" + username + "&tgsname=" + tgsname
				+ "&timestamp=" + convertTimeToUrl(getTimeStamp()) + "&macaddr=" + macaddress;
		String httpResponse = submitHttpGet(url);
		JSONObject jsonObject = stringToJsonObject(httpResponse);
		
		if(jsonObject == null){
			return null;
		}
		
		ASResponse response = ASResponse.parseJSONObject(jsonObject);
		return response;
	}
	
	public boolean createNewUser(String username, String password, String detail){        //create a new user
		String url = URL_PRE + URL_AUTH_SERVER;
		UserInfo user = new UserInfo(username, password, detail, getTimeStamp());
		JSONObject json = user.getPutJSON();
		int result = submitHttpPost(url, json, true);
		
		if(result == HTTP_RESPONSE_CODE_CREATED)
			return true;
		else
			return false;
	}
	
	public TGSResponse getServiceTicket(String serviceName, TGSTicket ticket, AuthenticatorC authenticator){        //Get the service ticket
		String url = URL_PRE + URL_TG_SERVER;
		TGSRequest request = new TGSRequest(serviceName, ticket, authenticator);
		JSONObject json = request.getPutJSON();
		String httpResponse = submitHttpPost(url, json);         //submit the request
		
		JSONObject jsonObject = stringToJsonObject(httpResponse);
		
		if(jsonObject == null){
			return null;
		}
		
		TGSResponse response = TGSResponse.parseJSONObject(jsonObject);
		return response;
	}
	
	/**
	 * @deprecated, use {@link getAllPOIsFromService_1V2()} instead
	 */
	@Deprecated
	public List<IncidentPOIs> getAllPOIsFromService_1(ServiceTicket ticket, AuthenticatorC auth){   //get pois from service 1
		String url = URL_PRE + URL_SERVICE_1;
		ServiceRequest request = new ServiceRequest(ticket, auth);
		JSONObject json = request.getPutJSON();
		String httpResponse = submitHttpPost(url, json);
		
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		
		List<IncidentPOIs> pois = IncidentPOIs.parceJSONArray(jsonArray);
		return pois;
	}
	
	/**
	 * @deprecated, use {@link getAllMSGsFromService_2V2()} instead
	 */
	@Deprecated
	public List<IncidentMSGs> getAllMSGsFromService_2(ServiceTicket ticket, AuthenticatorC auth){  //get messages from service 2
		String url = URL_PRE + URL_SERVICE_2;
		ServiceRequest request = new ServiceRequest(ticket, auth);
		JSONObject json = request.getPutJSON();
		String httpResponse = submitHttpPost(url, json);
		
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		
		List<IncidentMSGs> msgs = IncidentMSGs.parceJSONArray(jsonArray);
		return msgs;
	}
	
	
	/*******************  V2.0  ********************/
	/**
	 * Get all incidents
	 * @return a list of Incident Object
	 */
	public List<Incident> getAllIncidents(){
		String url = URL_PRE + URL_AUTH_SERVER + "?incidentTop=0";
		String httpResponse = submitHttpGet(url);
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		
		List<Incident> incidents = Incident.parceJSONArray(jsonArray);
		return incidents;
	}
	
	/**
	 * Create a incident
	 * @param incidentName The name of the incident
	 * @param detail Details, can be null
	 * @param securityLevel The security level of the incident, from 0 to 2
	 * @return
	 */
	public boolean createIncident(String incidentName, String detail, int securityLevel){
		String url = URL_PRE + URL_AUTH_SERVER + "?typeIncident=1";
		Incident incident = new Incident(incidentName, detail, securityLevel);
		JSONObject json = incident.getPutJSON();
		int result = submitHttpPost(url, json, true);
		
		if(result == HTTP_RESPONSE_CODE_CREATED)
			return true;
		else
			return false;
	}
	
	/**
	 * Create a new Point Of Interest for a incident
	 * @param incidentId Incident Id
	 * @param ticket Ticket of Service, should be null in Security Level 0
	 * @param auth Authenticator, should be in plaintext in Security Level 0 and ciphertext otherwise
	 * @param poi The Point of Interest to be sent
	 */
	public void createNewPOI(int incidentId, ServiceTicket ticket, AuthenticatorC auth, IncidentPOIs poi){
		String url = URL_PRE + URL_SERVICE_1 + "?incidentId=" + Integer.toString(incidentId);
		ServiceRequest request = new ServiceRequest(ticket, auth, poi);
		JSONObject json = request.getPutJSON();
		submitHttpPost(url, json);
	}
	
	/**
	 * Create a new Message for a incident
	 * @param incidentId Incident Id
	 * @param ticket Ticket of Service, should be null in Security Level 0
	 * @param auth Authenticator, should be in plaintext in Security Level 0 and ciphertext otherwise
	 * @param msg The Message to be sent
	 */
	public void createNewMSG(int incidentId, ServiceTicket ticket, AuthenticatorC auth, IncidentMSGs msg){
		String url = URL_PRE + URL_SERVICE_2 + "?incidentId=" + Integer.toString(incidentId);
		ServiceRequest request = new ServiceRequest(ticket, auth, msg);
		JSONObject json = request.getPutJSON();
		submitHttpPost(url, json);
	}
	
	/**
	 * Get All the POIs from service 1, version 2.0
	 * @param incidentId The incident Id to query
	 * @param ticket Ticket of service, null if security level is 0
	 * @param auth Authenticator, should be in plaintext in Security Level 0 and ciphertext otherwise
	 * @return A list of POIs
	 */
	public List<IncidentPOIs> getAllPOIsFromService_1V2(int incidentId, ServiceTicket ticket, AuthenticatorC auth){
		String url = URL_PRE + URL_SERVICE_1 + "?incidentId=" + Integer.toString(incidentId);
		ServiceRequest request = new ServiceRequest(ticket, auth);
		JSONObject json = request.getPutJSON();
		String httpResponse = submitHttpPost(url, json);
		
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		
		List<IncidentPOIs> pois = IncidentPOIs.parceJSONArray(jsonArray);
		return pois;
	}
	
	/**
	 * Get All Messages from service 2, version 2.0
	 * @param incidentId The incident Id to query
	 * @param ticket Ticket of service, null if security level is 0
	 * @param auth Authenticator, should be in plaintext in Security Level 0 and ciphertext otherwise
	 * @return A list of Message
	 */
	public List<IncidentMSGs> getAllMSGsFromService_2V2(int incidentId, ServiceTicket ticket, AuthenticatorC auth){
		String url = URL_PRE + URL_SERVICE_2 + "?incidentId=" + Integer.toString(incidentId);
		ServiceRequest request = new ServiceRequest(ticket, auth);
		JSONObject json = request.getPutJSON();
		String httpResponse = submitHttpPost(url, json);
		
		JSONArray jsonArray = stringToJsonArray(httpResponse);
		if(jsonArray == null){
			return null;
		}
		
		List<IncidentMSGs> msgs = IncidentMSGs.parceJSONArray(jsonArray);
		return msgs;
	}
	
	/*****************************************************************************/
	/******************  All the functions you need to use  **********************/
	/*****************************************************************************/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/******************* Do not have to care about these functions *******************/
	
	
	
	private static String convertTimeToUrl(String url){
		return url.replaceAll(" ", "%20");
	}
	
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	private JSONObject stringToJsonObject(String httpResponse){
		JSONObject jObject = null;
		try {
			if(httpResponse == null || httpResponse.equals("")){
				return null;
			}
			jObject = new JSONObject(httpResponse);
			return jObject;
		} catch (JSONException e) {
			Log.e("JSONarry", e.toString());
			e.printStackTrace();
			return null;
		}
	}
	
	private JSONArray stringToJsonArray(String httpResponse){
		JSONArray jArray = null;
		try {
			if(httpResponse == null || httpResponse.equals("")){
				return null;
			}
			jArray = new JSONArray(httpResponse);
			return jArray;
		} catch (JSONException e) {
			Log.e("JSONarry", e.toString());
			e.printStackTrace();
			return null;
		}
	}

	private String submitHttpGet(String url){
		HttpResponse response;
		
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url); 
		try {
			response = httpclient.execute(httpget);
			Log.i("GETcommand",response.getStatusLine().toString());
			
			if (response.getStatusLine().getStatusCode() != HTTP_RESPONSE_CODE_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
			
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release
			if (entity != null) {
				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				result = convertStreamToString(instream);
				Log.i("GETresult",result);
				instream.close();
			}
		} 
		catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e("httpGet", e.toString());
			return null;
		} catch (IOException e) {
			Log.e("httpGet", e.toString());
			e.printStackTrace();
			return null;
		} 
		return result;
	}
	
	private int submitHttpPost(String url, JSONObject json, boolean isCreate){
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
		HttpResponse response;
		
		try{			
			HttpPost post = new HttpPost(url);
			
			StringEntity se = new StringEntity(json.toString());
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(se);
			response = client.execute(post);
			/*check response to see if success*/
			
			if(response!=null){
				if (response.getStatusLine().getStatusCode() != HTTP_RESPONSE_CODE_CREATED) {
					throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
				}

				return response.getStatusLine().getStatusCode();
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			Log.e("httpPost", e.toString());
		}
		
		return 0;
	}
	
	private String submitHttpPost(String url, JSONObject json){
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
		HttpResponse response;
		String result = null;
		
		try{			
			HttpPost post = new HttpPost(url);
			
			StringEntity se = new StringEntity(json.toString());
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(se);
			response = client.execute(post);
			
			if (response.getStatusLine().getStatusCode() != HTTP_RESPONSE_CODE_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
			
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				result = convertStreamToString(instream);
				Log.i("GETresult",result);
				instream.close();
			}
		}catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e("httpGet", e.toString());
			return null;
		} catch (IOException e) {
			Log.e("httpGet", e.toString());
			e.printStackTrace();
			return null;
		} 
		return result;
	}
	
}
