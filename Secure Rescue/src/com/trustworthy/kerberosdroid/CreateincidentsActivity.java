package com.trustworthy.kerberosdroid;

import java.util.List;

import com.trustworthy.kerberosapi.HTTP.KerberosClient;
import com.trustworthy.kerberosapi.Models.Incident;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateincidentsActivity extends Activity {
	
	private Button btn_create_incidents;
	private EditText edit_name;
	private EditText edit_securitylevel;
	String incidentname;
	int incidentsecuritylevel;
	KerberosClient client;
	public boolean inc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_incidents);
		
		btn_create_incidents = (Button)findViewById(R.id.create_incidents);
		edit_name = (EditText)findViewById(R.id.edit_name);
		edit_securitylevel = (EditText)findViewById(R.id.edit_securitylevel);
		
		btn_create_incidents.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				    
				if (!edit_name.getText().toString().equals("") && !edit_securitylevel.getText().toString().equals("")){
					incidentname = edit_name.getText().toString();
				    incidentsecuritylevel = Integer.valueOf(edit_securitylevel.getText().toString());
				    CreateThread thread = new CreateThread();
                    thread.start();    
                    finish();
				}
				else {
					Toast.makeText(getApplicationContext(), "Please check your input content!", Toast.LENGTH_LONG).show();
				}
	
				
			}

		});

}
	
	public class CreateThread extends Thread{
		
		@Override
		public void run(){
			
			client = new KerberosClient();
			inc = client.createIncident(incidentname, null, incidentsecuritylevel);
			CreateHandler.sendEmptyMessage(1);
		}

	}
	
	
	private Handler CreateHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 1:
			if (inc == true)
			{
				//Intent CreateincIntent = new Intent(getApplicationContext(), SelectActivity.class);
			    //startActivity(CreateincIntent);
			    Toast.makeText(getApplicationContext(), "New incident created!", Toast.LENGTH_LONG).show();
	
			}
			else{
				Toast.makeText(getApplicationContext(), "Fail to create new incident!", Toast.LENGTH_LONG).show();
			}
			
			break;
			}
	
		}
	};

}