package com.trustworthy.kerberosdroid;

import com.trustworthy.kerberosapi.HTTP.KerberosClient;
import com.trustworthy.kerberosapi.Models.AuthenticatorC;
import com.trustworthy.kerberosapi.Models.IncidentPOIs;
import com.trustworthy.kerberosapi.Models.ServiceTicket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PostPOIs extends Activity {

	private EditText pois_postname;
	private EditText pois_postlocation;
	private Button btn_postpois;
	private TextView text_inc_pois_level;
	private RadioGroup radio_group;
	private RadioButton rbtn_landmarks, rbtn_victim, rbtn_responder, rbtn_commander;
	int incidend, inclevelend, poisid, poistype;
	String poisname, poislocation;
	AuthenticatorC auth0 ,auth1;
	ServiceTicket tic_service1;
	KerberosClient clientx;
	IncidentPOIs poi;
	WifiInfo info;
	String userp, timestamp0, keyp;


	@Override
	protected void onCreate(Bundle saveInstanceState) {
		
		super.onCreate(saveInstanceState);
		setContentView(R.layout.post_pois);
		

		pois_postname = (EditText)findViewById(R.id.pois_postname);
		pois_postlocation = (EditText)findViewById(R.id.pois_postlocation);
		btn_postpois = (Button)findViewById(R.id.btn_postpois);
		text_inc_pois_level = (TextView)findViewById(R.id.text_inc_pois_level);
		radio_group = (RadioGroup)findViewById(R.id.radio_group);
		rbtn_landmarks = (RadioButton)findViewById(R.id.rbtn_landmarks);
		rbtn_victim = (RadioButton)findViewById(R.id.rbtn_victim);
		rbtn_responder = (RadioButton)findViewById(R.id.rbtn_responder);
		rbtn_commander = (RadioButton)findViewById(R.id.rbtn_commander);
		//poisid = Integer.valueOf(pois_postid.getText().toString());
		
		
		Intent PostPOIsIntent = getIntent();
		Bundle PostPOIsBundle = PostPOIsIntent.getExtras();
	
		userp = PostPOIsBundle.getString("user");
		keyp = PostPOIsBundle.getString("key_c_v3");
		incidend = PostPOIsBundle.getInt("incids");
		inclevelend = PostPOIsBundle.getInt("inclevels");
		//auth0 = (AuthenticatorC) PostPOIsBundle.getSerializable("authenticator0");
		//auth1 = (AuthenticatorC) PostPOIsBundle.getSerializable("authenticator1");
		tic_service1 = (ServiceTicket) PostPOIsBundle.getSerializable("ticket_service3");
	
	    text_inc_pois_level.setText(Integer.toString(incidend));
	    
	    radio_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == rbtn_landmarks.getId()){
					poistype = 0;
				}
				if(checkedId == rbtn_victim.getId()){
					poistype = 1;
				}
				if(checkedId == rbtn_responder.getId()){
					poistype = 2;
				}
				if(checkedId == rbtn_commander.getId()){
					poistype = 3;
				}
			}
	    });
	
		btn_postpois.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                 
	            info = wifi.getConnectionInfo();
	                   
	            info.getMacAddress();
	            if (!pois_postname.getText().toString().equals("") && !pois_postlocation.getText().toString().equals(""))
	            {
				poisname = pois_postname.getText().toString();
				poislocation = pois_postlocation.getText().toString();
				PostPOIsThread thread = new PostPOIsThread();
	            thread.start();     	
		
	            finish();
	            }
	            else
	            {
	            Toast.makeText(getApplicationContext(), "Please complete your input!", Toast.LENGTH_LONG).show();	
	            }
			}

		});

	}
	
	 
	public class PostPOIsThread extends Thread{
		
		@Override
		public void run(){

			clientx = new KerberosClient();
			poi = new IncidentPOIs(poistype, poisname, poislocation, poistype);
			timestamp0 = KerberosClient.getTimeStamp();
			if(inclevelend == 0){
			auth0 = new AuthenticatorC(userp, info.getMacAddress().toString(), timestamp0);
			clientx.createNewPOI(incidend, null, auth0, poi);	
			}
          	if(inclevelend == 1 || inclevelend == 2){
          	auth1 = new AuthenticatorC(userp, info.getMacAddress().toString(), timestamp0);
          	auth1.seal(keyp);
          	poi.encrypt(keyp);
          	clientx.createNewPOI(incidend, tic_service1, auth1, poi);
          	}
			PostPOIsHandler.sendEmptyMessage(1);
	}
}
	private Handler PostPOIsHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 1:
				//Intent PostpoisIntent = new Intent(getApplicationContext(), MainActivity.class);
				//startActivity(PostpoisIntent);
    			Toast.makeText(getApplicationContext(), "New POIs posted!", Toast.LENGTH_LONG).show();
         
				break;
			}
		}
	};
}
