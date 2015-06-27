package com.trustworthy.kerberosdroid;

import java.security.MessageDigest;

import com.trustworthy.kerberosapi.HTTP.KerberosClient;
import com.trustworthy.kerberosapi.Models.ASResponse;
import com.trustworthy.kerberosapi.Models.AuthenticatorC;
import com.trustworthy.kerberosapi.Models.ServiceTicket;
import com.trustworthy.kerberosapi.Models.TGSResponse;
import com.trustworthy.kerberosapi.Models.TGSTicket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	

	private EditText username_login;
	private EditText password_login;
	private TextView inc_name;
	private TextView inc_level;
	private Button btn_login;
	private Button btn_signup;
	String incname, userj, passj, passmd5j, key_C_TGS, timestamp2, lifetime, timestamp3, key_c_v1, servicename1, timestamp4, key_c_v2,
	servicename2, timestamp5;
	int incid, inclevel, author, errorCode0, errorCode1, errorCode2;
	AuthenticatorC authenticator1;
	TGSResponse response_TGS1, response_TGS2;
	ServiceTicket ticket_service1, ticket_service2;
	boolean god;
	TGSTicket ticket_tgs;
	KerberosClient client;
	WifiInfo info;
	ASResponse response_AS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		username_login = (EditText)findViewById(R.id.username_login);
		password_login = (EditText)findViewById(R.id.password_login);
		btn_login = (Button)findViewById(R.id.btn_login);
		btn_signup = (Button)findViewById(R.id.btn_signup);
		inc_name = (TextView)findViewById(R.id.inc_name);
		inc_level = (TextView)findViewById(R.id.inc_level);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
        	
		incid = bundle.getInt("incidx");
		inclevel = bundle.getInt("inclevelx");
		incname = bundle.getString("incnamex");

		inc_name.setText(incname);
		inc_level.setText(Integer.toString(inclevel));
		
		if (inclevel == 0){
			password_login.setEnabled(false);
		}

		btn_login.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(inclevel == 0){
				
				userj=username_login.getText().toString();
				WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                
                info = wifi.getConnectionInfo();
                   
                info.getMacAddress();
                  
                if(!userj.equals(""))
                {
                	LoginThread thread = new LoginThread();
                	thread.start();
                	
                }
                else
                {
                	Toast.makeText(getApplicationContext(), "Username can not be empty!", Toast.LENGTH_LONG).show();
				}
			}
				
				if(inclevel == 1 || inclevel == 2){
				//SharedPreferences pre = getSharedPreferences("signupvalue", Context.MODE_PRIVATE);  
                //String signupuser = pre.getString("username_signup", null);  
                //String signuppass = pre.getString("password_signup", null);   
				userj=username_login.getText().toString();
		        passj=password_login.getText().toString();
		        
                passmd5j = MD5(MD5(passj)+ userj);
				
				WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                
                info = wifi.getConnectionInfo();
                   
                info.getMacAddress();
                
		        
		        if(!userj.equals("")&&!passj.equals(""))  
                {  
		        	
			           LoginThread thread = new LoginThread();
		        	   thread.start();
                
		        	   if(errorCode0 == 0){
		        	   if(god == true){
		        	   Toast.makeText(getApplicationContext(), "Wrong password!", Toast.LENGTH_LONG).show();
		        	   }
		        	   }
		        	   else
		        	   {
		        	   Toast.makeText(getApplicationContext(), "Wrong username!", Toast.LENGTH_LONG).show();
		        	   }
               }else
               {
            	   Toast.makeText(getApplicationContext(), "Please comlete your input!", Toast.LENGTH_LONG).show();  
               }  
			}
				
			
			}
		});
		
		
		
		btn_signup.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent SignupIntent = new Intent(getApplicationContext(), CreateaccountActivity.class);
			    startActivity(SignupIntent);
			}
			
		});
	
	}

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

	
	public class LoginThread extends Thread{
		
		@Override
		public void run(){
			client = new KerberosClient();
			if(inclevel == 0){
				
			LoginHandler.sendEmptyMessage(1);	
			}
			
			if(inclevel == 1 || inclevel == 2){
   
          	response_AS = client.getTGSTicketFromServer(userj, "TGS", info.getMacAddress().toString());
			
          	errorCode0 = response_AS.getErrorCode();
          	
          	if (errorCode0 == 0){
          	god = response_AS.unSeal(passmd5j);
          	
          	if (god == true){
          	author = response_AS.getAuthority();
          	ticket_tgs = response_AS.getTicket();
        	//解密后，取得Kctgs,即session key;
          	
        	key_C_TGS = response_AS.getKey_C_TGS();
        	timestamp2 = response_AS.getTimestamp();
        	lifetime = response_AS.getLifetime();
        	
        	//判断ErrorCode是否为0;
        	//client得到session key, ticket_tgs, but can not see the ticket inside dut to not having Ktgs, the TGS secret key;
        	timestamp3 = KerberosClient.getTimeStamp();
        	authenticator1 = new AuthenticatorC(userj, info.getMacAddress().toString(), timestamp3);
        	//use session key ke_c_TGS encrypt the authenticator 
        	authenticator1.seal(key_C_TGS);

        	response_TGS1 = client.getServiceTicket("Service_1", ticket_tgs, authenticator1);
        	//client decrypts using key_C_TGS;
        	response_TGS1.unSeal(key_C_TGS);
        	key_c_v1 = response_TGS1.getKey_c_v();
        	servicename1 = response_TGS1.getServicename();
        	timestamp4 = response_TGS1.getTimestamp();
        	ticket_service1 = response_TGS1.getTicket();
        	errorCode1 = response_TGS1.getErrorCode();
        	
        	response_TGS2 = client.getServiceTicket("Service_2", ticket_tgs, authenticator1);
          	//client decrypts using key_C_TGS;
            response_TGS2.unSeal(key_C_TGS);
            key_c_v2 = response_TGS2.getKey_c_v();
            servicename2 = response_TGS2.getServicename();
            timestamp5 = response_TGS2.getTimestamp();
            ticket_service2 = response_TGS2.getTicket();
            errorCode2 = response_TGS2.getErrorCode();
            
			LoginHandler.sendEmptyMessage(1);
          	}
          	else
          	{
          		
          		return;
          	}
          	}
          	else
          	{
          		return;	
          	}
			}
		}
	}
	
	
	private Handler LoginHandler = new Handler() {
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 1:
				Bundle LoginBundle = new Bundle();
				Intent LoginIntent = new Intent(getApplicationContext(), MainActivity.class);
				
				if(inclevel == 0)
				{
				   LoginBundle.putString("userj", userj);
	               LoginBundle.putString("passj", passj);
	               LoginBundle.putInt("incid", incid);
	               LoginBundle.putInt("inclevel", inclevel);
	               LoginBundle.putInt("author", author);
	               LoginIntent.putExtras(LoginBundle);
	               startActivity(LoginIntent);
            	   Toast.makeText(getApplicationContext(), "Login successfully!", Toast.LENGTH_LONG).show();
				}
				if(inclevel == 1 || inclevel == 2)
				{
				  
                   LoginBundle.putString("userj", userj);
                   LoginBundle.putString("passj", passj);
                   LoginBundle.putInt("incid", incid);
                   LoginBundle.putInt("inclevel", inclevel);
                   LoginBundle.putInt("author", author);
                   LoginBundle.putString("key_c_v1", key_c_v1);
                   LoginBundle.putString("key_c_v2", key_c_v2);
                   LoginBundle.putSerializable("ticket_service1", ticket_service1);
                   LoginBundle.putSerializable("ticket_service2", ticket_service2); 
                   LoginIntent.putExtras(LoginBundle);
                   startActivity(LoginIntent);
            	   Toast.makeText(getApplicationContext(), "Login successfully!", Toast.LENGTH_LONG).show();
				   }
			
				break;
		}
		}
		
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
