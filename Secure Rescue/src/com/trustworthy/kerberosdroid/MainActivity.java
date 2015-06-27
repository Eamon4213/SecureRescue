package com.trustworthy.kerberosdroid;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import com.trustworthy.kerberosapi.HTTP.KerberosClient;
import com.trustworthy.kerberosapi.Models.ASResponse;
import com.trustworthy.kerberosapi.Models.AuthenticatorC;
import com.trustworthy.kerberosapi.Models.IncidentMSGs;
import com.trustworthy.kerberosapi.Models.IncidentPOIs;
import com.trustworthy.kerberosapi.Models.ServiceTicket;
import com.trustworthy.kerberosapi.Models.TGSResponse;
import com.trustworthy.kerberosapi.Models.TGSTicket;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Button btn_get;
	private Button btn_post;
	private TextView idinfor;
	private TextView authorityinfor;
	String user;
	String pass;
	String passmd5;
	String macad;
	WifiInfo info;
	ASResponse response_AS;
	List<IncidentMSGs> msgs;
	List<IncidentPOIs> pois;
	String timestamp0, timestamp6, key_c_v3, key_c_v4;
	int errorCode1, errorCode2, incidx;
	TGSTicket ticket_tgs;
	AuthenticatorC authenticator0;
	TGSResponse response_TGS;
	AuthenticatorC authenticator2;
	ServiceTicket ticket_service3, ticket_service4;
	KerberosClient client;
	IncidentMSGs msg;
	IncidentPOIs poi;
	int incids, inclevels, authors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn_get = (Button)findViewById(R.id.btn_get);
		btn_post = (Button)findViewById(R.id.btn_post);
		idinfor = (TextView)findViewById(R.id.idinfor);
		authorityinfor = (TextView)findViewById(R.id.authorityinfor);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		Intent LoginIntent = getIntent();
        Bundle LoginBundle = LoginIntent.getExtras();
        //½«userj = user passj = pass
        user = LoginBundle.getString("userj");
        pass = LoginBundle.getString("passj");
        incids = LoginBundle.getInt("incid");
        inclevels = LoginBundle.getInt("inclevel");
        authors = LoginBundle.getInt("author");
        key_c_v3 = LoginBundle.getString("key_c_v1");
        key_c_v4 = LoginBundle.getString("key_c_v2");
        ticket_service3 = (ServiceTicket) LoginBundle.getSerializable("ticket_service1");
        ticket_service4 = (ServiceTicket) LoginBundle.getSerializable("ticket_service2");
        
        
        idinfor.setText(user);
        authorityinfor.setText(Integer.toString(authors));

		btn_post.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog();
			}

		});
		
		btn_get.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog2();
			}
			
		});
		
	}

	private void dialog2()
	{
		new AlertDialog.Builder(MainActivity.this).
		setSingleChoiceItems(R.array.get_list_item, 0, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog2, int arg1) {
				// TODO Auto-generated method stub
				String st[] =getResources().getStringArray(R.array.get_list_item);
				switch (arg1) {
				case 0:
					if(inclevels == 1 || inclevels == 2){
			               passmd5 = MD5(MD5(pass) + user);  
			     
			      	       WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			      	                  
			      	       info = wifi.getConnectionInfo();
			      	                   
			      	       info.getMacAddress();
			      	              	
			      	       GetPOIsThread thread = new GetPOIsThread();
			  	           thread.start();
						}
						if(inclevels == 0){
							WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			                  
				      	    info = wifi.getConnectionInfo();
				      	                   
				      	    info.getMacAddress();
				      	              	
				      	    GetPOIsThreadlevel0 thread = new GetPOIsThreadlevel0();
				  	        thread.start();
							
						}
					break;
				case 1:
					if(inclevels == 1|| inclevels == 2){
		                passmd5 = MD5(MD5(pass) + user);  
		             
		                WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		                  
		                info = wifi.getConnectionInfo();
		                   
		                info.getMacAddress();
		  
		                GetMSGsThread thread = new GetMSGsThread();
		        		thread.start();
					}
					if(inclevels == 0){
						WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		                  
			      	    info = wifi.getConnectionInfo();
			      	                   
			      	    info.getMacAddress();
			      	              	
			      	    GetMSGsThreadlevel0 thread = new GetMSGsThreadlevel0();
			  	        thread.start();
					}
					break;
				}
				dialog2.dismiss();
			}
			
		}).show();
	}
	
	private void dialog()
	
	{
		   new AlertDialog.Builder(MainActivity.this).
           setSingleChoiceItems(R.array.post_list_item, 0, new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String st[]=getResources().getStringArray(R.array.post_list_item);
                switch (which) {
                
                case 0:
                	if(inclevels == 1 || inclevels == 2){
                		Bundle PostPOIsBundle = new Bundle();
                	    PostPOIsBundle.putInt("incids", incids);
                	    PostPOIsBundle.putInt("inclevels", inclevels);
                	    PostPOIsBundle.putString("user", user);
                	    PostPOIsBundle.putString("key_c_v3", key_c_v3);
                	   // PostPOIsBundle.putSerializable("authenticator2", authenticator2);
                	    PostPOIsBundle.putSerializable("ticket_service3", ticket_service3);
                	    Intent PostPOIsIntent = new Intent(getApplicationContext(), PostPOIs.class);
                	    PostPOIsIntent.putExtras(PostPOIsBundle);
                	    startActivity(PostPOIsIntent);
                	}
                	if(inclevels == 0){
                		Bundle PostPOIsBundle = new Bundle();
                		PostPOIsBundle.putInt("incids", incids);
                		PostPOIsBundle.putInt("inclevels", inclevels);
                		PostPOIsBundle.putString("user", user);
                		//PostPOIsBundle.putSerializable("authenticator0", authenticator0);
                		Intent PostPOIsIntent = new Intent(getApplicationContext(), PostPOIs.class);
                		PostPOIsIntent.putExtras(PostPOIsBundle);
                		startActivity(PostPOIsIntent);
                	}
                    break;

                case 1:
                	if(inclevels == 1 || inclevels ==2){
                	Bundle PostMSGsBundle = new Bundle();
                	PostMSGsBundle.putInt("incids", incids);
                	PostMSGsBundle.putInt("inclevels", inclevels);
                	//PostMSGsBundle.putSerializable("authenticator2", authenticator2);
                	PostMSGsBundle.putSerializable("ticket_service4", ticket_service4);
                	PostMSGsBundle.putString("key_c_v4", key_c_v4);
                	PostMSGsBundle.putString("user", user);
                    Intent PostMSGsIntent = new Intent(getApplicationContext(), PostMSGs.class);
                    PostMSGsIntent.putExtras(PostMSGsBundle);
                    startActivity(PostMSGsIntent);
                	}
                	if(inclevels == 0){
                		Bundle PostMSGsBundle = new Bundle();
                		PostMSGsBundle.putInt("incids", incids);
                		PostMSGsBundle.putInt("inclevels", inclevels);
                		PostMSGsBundle.putString("user", user);
                		//PostMSGsBundle.putSerializable("authenticator0", authenticator0);
                		Intent PostMSGsIntent = new Intent(getApplicationContext(), PostMSGs.class);
                        PostMSGsIntent.putExtras(PostMSGsBundle);
                        startActivity(PostMSGsIntent);}
                    break;
            
                }
                dialog.dismiss();
            }
        }).show();
		
		
	}
	
	public class GetPOIsThread extends Thread{
		
		@Override
		public void run(){
		  	  
			    client = new KerberosClient();	
  
            	timestamp6 = KerberosClient.getTimeStamp();
            	authenticator2 = new AuthenticatorC(user, info.getMacAddress().toString(), timestamp6);
            	authenticator2.seal(key_c_v3);
            	pois = client.getAllPOIsFromService_1V2(incids, ticket_service3, authenticator2);
            	
             	for (int i = 0; i < pois.size(); i++) {
    				
              		poi = pois.get(i);
              		
              		poi.decrypt(key_c_v3);
              		
    			}   

            	GetPOIsHandler.sendEmptyMessage(1);
		}
	}
	
	public class GetPOIsThreadlevel0 extends Thread{
		
		@Override
		public void run(){
			
			client = new KerberosClient();
			timestamp0 = KerberosClient.getTimeStamp();
			authenticator0 = new AuthenticatorC(user, info.getMacAddress().toString(), timestamp0);
			pois = client.getAllPOIsFromService_1V2(incids, null, authenticator0);
			GetPOIsHandler.sendEmptyMessage(1);
			
		}
	
	}
	
   public class GetMSGsThread extends Thread{
		
		@Override
		public void run(){
		
			client = new KerberosClient();       
            timestamp6 = KerberosClient.getTimeStamp();
            authenticator2 = new AuthenticatorC(user, info.getMacAddress().toString(), timestamp6);
            authenticator2.seal(key_c_v4);	
          	msgs = client.getAllMSGsFromService_2V2(incids, ticket_service4, authenticator2);
          	
          	for (int j = 0; j < msgs.size(); j++) {
				
          		msg = msgs.get(j);
          		
          		msg.decrypt(key_c_v4);
          		
			}   

			
			GetMSGsHandler.sendEmptyMessage(1);
		}
	}
	
  
	public class GetMSGsThreadlevel0 extends Thread{
		
		@Override
		public void run(){
			
			client = new KerberosClient();
			timestamp0 = KerberosClient.getTimeStamp();
			authenticator0 = new AuthenticatorC(user, info.getMacAddress().toString(), timestamp0);
			msgs = client.getAllMSGsFromService_2V2(incids, null, authenticator0);

			GetMSGsHandler.sendEmptyMessage(1);
			
		}
	
	}
	
   private Handler GetPOIsHandler = new Handler() {
	   @Override
	   public void handleMessage(Message msg){
		   switch(msg.what){
		   case 1:
			   
				Bundle bundle = new Bundle();
	        		
				bundle.putString("user", user);
	            bundle.putSerializable("pois", (Serializable) pois);
	    
	            Intent intent = new Intent(getApplicationContext(), ResultPOIs.class); 
	                  
	            intent.putExtras(bundle);
	                 
	            startActivity(intent);   

	            break;
		   }
	   }
   };
   
   
   
   private Handler GetMSGsHandler = new Handler() {
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 1:
	           	Bundle bundle = new Bundle();
	           	
	           	bundle.putString("user", user);
	        		
	            bundle.putSerializable("msgs", (Serializable) msgs);
	        		
                Intent intent = new Intent(getApplicationContext(), ResultMSGs.class);
                intent.putExtras(bundle);
                  
                startActivity(intent); 
              	
				break;
			}
		}
	};
	
	
	public static String MD5(String str)  
    {  
        MessageDigest md5 = null;  
        try  
        {  
            md5 = MessageDigest.getInstance("MD5");  
        }catch(Exception e)  
        {  
            e.printStackTrace();  
            return "";  
        }  
          
        char[] charArray = str.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
          
        for(int i = 0; i < charArray.length; i++)  
        {  
            byteArray[i] = (byte)charArray[i];  
        }  
        byte[] md5Bytes = md5.digest(byteArray);  
          
        StringBuffer hexValue = new StringBuffer();  
        for( int i = 0; i < md5Bytes.length; i++)  
        {  
            int val = ((int)md5Bytes[i])&0xff;  
            if(val < 16)  
            {  
                hexValue.append("0");  
            }  
            hexValue.append(Integer.toHexString(val));  
        }  
        return hexValue.toString();  
    }  
	


}




