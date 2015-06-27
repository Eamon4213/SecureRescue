package com.trustworthy.kerberosdroid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trustworthy.kerberosapi.HTTP.KerberosClient;
import com.trustworthy.kerberosapi.Models.Incident;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SelectActivity extends Activity{
	
	private Button btn_select;
	private Button btn_create_incidents;
	List<Incident> incidentsx;
	KerberosClient clientx;
	int incidx, inclevelx;
	String incnamex;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);

		btn_select = (Button)findViewById(R.id.btn_select);
		btn_create_incidents = (Button)findViewById(R.id.btn_create_incidents);
		
		btn_create_incidents.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	
				Intent SelectIntent = new Intent(getApplicationContext(), CreateincidentsActivity.class);
				startActivity(SelectIntent); 

			}
			
		});
	
		btn_select.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
	
				SelectThread thread = new SelectThread();
        		
				thread.start();
				
				
				
				
			}
	                    
		});
	}
	
	public class SelectThread extends Thread{
		
		@Override
		public void run(){
	
			clientx = new KerberosClient();
			incidentsx = clientx.getAllIncidents();
			SelectHandler.sendEmptyMessage(1);
	
		}

	}


	private Handler SelectHandler = new Handler() {
		
		@Override 
		public void handleMessage(Message msg) {
			switch(msg.what){
			
			case 1:
				AlertDialog.Builder builder = new AlertDialog.Builder(SelectActivity.this);   

				LayoutInflater factory = LayoutInflater.from(getApplicationContext());  

				final View textEntryView = factory.inflate(R.layout.result_incidents, null);  

				builder.setTitle("Please Select an Incident");  
				
				builder.setView(textEntryView);

				ListView listView = (ListView)textEntryView.findViewById(R.id.list_incidents);

				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

				 	for (int k= 0; k< incidentsx.size(); k++) {

				 			Incident incidentk = incidentsx.get(k);

				 		    Map<String, Object> map = new HashMap<String, Object> ();
				 		    
				 		    incidx = incidentk.getIncidentId();
				 		    incnamex = incidentk.getIncidentName();
				 		    inclevelx = incidentk.getSecurityLevel();

				 		    String inId_str = Integer.toString(incidx);
				 		    String inLevel_str = Integer.toString(inclevelx);
				 		    String inName_str = incnamex;
				 		    map.put("incidents_id", inId_str);
				 		    map.put("incidents_level", inLevel_str);
				 		    map.put("incidents_name", inName_str);

				 		    list.add(map);
				 		}

				 	    SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), list, R.layout.listview_item_incidents, new String[]{
					 		"incidents_name", "incidents_id"}, new int [] {R.id.incidents_name, R.id.incidents_id});
			
				 		listView.setAdapter(adapter);
			
				 		listView.setOnItemClickListener(new OnItemClickListener(){

							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
								// TODO Auto-generated method stub
								HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
								incidx = Integer.valueOf((String)item.get("incidents_id"));
								inclevelx = Integer.valueOf((String)item.get("incidents_level"));
								incnamex = (String)item.get("incidents_name");
								
								Bundle bundle = new Bundle();
								bundle.putInt("incidx", incidx);
								bundle.putInt("inclevelx", inclevelx);
								bundle.putString("incnamex", incnamex);
								Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				         	    intent.putExtras(bundle);
				         	   
				         	    startActivity(intent);
				         	    
				         	    Toast.makeText(getApplicationContext(), "Get it!", Toast.LENGTH_LONG).show();
					
							}

						});
				
	
        		builder.create().show();  
        		       
        		
	
 				break;

			}
		
			
		}
		
	
	};



}
