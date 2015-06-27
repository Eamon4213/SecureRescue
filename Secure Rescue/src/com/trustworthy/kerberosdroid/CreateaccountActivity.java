package com.trustworthy.kerberosdroid;

import java.io.Serializable;
import java.security.MessageDigest;

import com.trustworthy.kerberosapi.HTTP.KerberosClient;
import com.trustworthy.kerberosapi.Models.ASResponse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateaccountActivity extends Activity {

	private EditText username_signup;
	private EditText password_signup;
	private EditText confirm_password;
	private TextView create_account;
	private Button btn_create;
	String pass1, pass2, useri, passi, passmd5r;
	int errorCode;
	WifiInfo info;
	ASResponse response_AS;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createaccount);

		username_signup = (EditText)findViewById(R.id.username_signup);
		password_signup = (EditText)findViewById(R.id.password_signup);
		confirm_password = (EditText)findViewById(R.id.confirm_password);
		create_account = (TextView)findViewById(R.id.create_account);
		btn_create = (Button)findViewById(R.id.btn_create);
		
		
		btn_create.setOnClickListener(new Button.OnClickListener()
		{
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//SharedPreferences pre = getSharedPreferences("signupvalue", Context.MODE_PRIVATE);
				if (!username_signup.getText().toString().equals("") && !password_signup.getText().toString().equals("") && !confirm_password.getText().toString().equals(""))
				{
				pass1 = password_signup.getText().toString();
		        pass2= confirm_password.getText().toString();
				
				if(pass1.equals(pass2)){
					
					//pre.edit().putString("username_signup", username_signup.getText().toString()).  
		        	//putString("password",passi).commit();
					useri = username_signup.getText().toString();
					passi = pass2;
					passmd5r = MD5(MD5(passi)+ useri);
				
				    WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                
                    info = wifi.getConnectionInfo();
                   
                    info.getMacAddress();
                    
                    SubmitxThread thread = new SubmitxThread();
                    thread.start(); 
				
					finish();
					
				}
				else 
				{
					Toast.makeText(getApplicationContext(), "Please confirm your password!", Toast.LENGTH_LONG).show();
				}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Please complete your input!", Toast.LENGTH_LONG).show();
				}
			}
			
		});
	
	}
public class SubmitxThread extends Thread{
		
		@Override
		public void run(){
			KerberosClient client = new KerberosClient();
          	
          	client.createNewUser(useri, passmd5r, null);
          	
          	response_AS = client.getTGSTicketFromServer(useri, "TGS", info.getMacAddress().toString());
          	
         	response_AS.unSeal(passmd5r);
          	
          	//TGSTicket ticket_tgs = response_AS.getTicket();
          	
          	errorCode = response_AS.getErrorCode();
			
			SubmitxHandler.sendEmptyMessage(1);
		}
	}

    private Handler SubmitxHandler = new Handler() {
    	@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 1:
				if( errorCode == 0 )  
                {  
					finish();
    			    Toast.makeText(getApplicationContext(), "New account created!", Toast.LENGTH_LONG).show();
                    
                }else  
                {                 
                   Toast.makeText(getApplicationContext(), "Fail to create an account!", Toast.LENGTH_LONG).show();  
                }  
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
