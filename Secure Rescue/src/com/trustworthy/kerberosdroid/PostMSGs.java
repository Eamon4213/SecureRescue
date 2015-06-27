package com.trustworthy.kerberosdroid;

import com.trustworthy.kerberosapi.HTTP.KerberosClient;
import com.trustworthy.kerberosapi.Models.AuthenticatorC;
import com.trustworthy.kerberosapi.Models.IncidentMSGs;
import com.trustworthy.kerberosapi.Models.ServiceTicket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PostMSGs extends Activity {

	private EditText msgs_posttitle;
	private EditText msgs_postmessage;
	private EditText msgs_postlevel;
    private Button btn_postmsgs;
    private TextView text_inc_msgs_level;
    int msgsid, msgslevel, incidend, inclevelend;
    String msgstitle, msgsmessage;
    AuthenticatorC auth0, auth2;
    ServiceTicket tic_service2;
    KerberosClient clientx;
    IncidentMSGs msg;
    WifiInfo info;
    String userp, timestamp0, keyp;
    
	@Override
	protected void onCreate(Bundle saveInstanceState) {
		
		super.onCreate(saveInstanceState);
		setContentView(R.layout.post_msgs);
		
		msgs_posttitle = (EditText)findViewById(R.id.msgs_posttitle);
		msgs_postmessage = (EditText)findViewById(R.id.msgs_postmessage);
		msgs_postlevel = (EditText)findViewById(R.id.msgs_postlevel);
		btn_postmsgs = (Button)findViewById(R.id.btn_postmsgs);
		text_inc_msgs_level = (TextView)findViewById(R.id.text_inc_msgs_level);
		//msgsid = Integer.valueOf(msgs_postid.getText().toString());
	
		Intent PostMSGsIntent = getIntent();
		Bundle PostMSGsBundle = PostMSGsIntent.getExtras();
	
		userp = PostMSGsBundle.getString("user");
		keyp = PostMSGsBundle.getString("key_c_v4");
		incidend = PostMSGsBundle.getInt("incids");
		inclevelend = PostMSGsBundle.getInt("inclevels");
		//auth0 = (AuthenticatorC) PostMSGsBundle.getSerializable("authenticator0");
		//auth2 = (AuthenticatorC) PostMSGsBundle.getSerializable("authenticator2");
		tic_service2 = (ServiceTicket) PostMSGsBundle.getSerializable("ticket_service4");
	
	
		text_inc_msgs_level.setText(Integer.toString(incidend));
		
		btn_postmsgs.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                
				info = wifi.getConnectionInfo();
					                   
				info.getMacAddress();
				if (!msgs_postlevel.getText().toString().equals("") && !msgs_posttitle.getText().toString().equals("") && !msgs_postmessage.getText().toString().equals(""))
				{
				msgslevel = Integer.valueOf(msgs_postlevel.getText().toString());
				if( msgslevel == 0 || msgslevel == 1 || msgslevel == 2)
				{
				msgstitle = msgs_posttitle.getText().toString();
				msgsmessage = msgs_postmessage.getText().toString();
				PostMSGsThread thread = new PostMSGsThread();
	            thread.start();     
	            
	            finish();
				}
				else
				{
				Toast.makeText(getApplicationContext(), "Please check the secret level!", Toast.LENGTH_LONG).show();	
				}
				}
				else
				{
				Toast.makeText(getApplicationContext(), "Please complete your input!", Toast.LENGTH_LONG).show();	
				}
		
			}


		});

	}
	 
	public class PostMSGsThread extends Thread{
		
		@Override
		public void run(){

			clientx = new KerberosClient();
			msg = new IncidentMSGs(msgstitle, msgsmessage, msgslevel);
			timestamp0 = KerberosClient.getTimeStamp();
			if(inclevelend == 0){
			auth0 = new AuthenticatorC(userp, info.getMacAddress().toString(), timestamp0);
			clientx.createNewMSG(incidend, null, auth0, msg);	
			}
          	if(inclevelend == 1 || inclevelend == 2){
          	auth2 = new AuthenticatorC(userp, info.getMacAddress().toString(), timestamp0);
          	auth2.seal(keyp);
          	msg.encrypt(keyp);
          	clientx.createNewMSG(incidend, tic_service2, auth2, msg);
          	}
			PostMSGsHandler.sendEmptyMessage(1);
	}
}
	private Handler PostMSGsHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 1:
				
				//Intent PostmsgsIntent = new Intent(getApplicationContext(), MainActivity.class);
				//startActivity(PostmsgsIntent);
    			Toast.makeText(getApplicationContext(), "New MSGs posted!", Toast.LENGTH_LONG).show();
         
				break;
			}
		}
	};
}
