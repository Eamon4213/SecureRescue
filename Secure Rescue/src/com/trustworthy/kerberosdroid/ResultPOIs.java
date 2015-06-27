package com.trustworthy.kerberosdroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trustworthy.kerberosapi.Models.IncidentMSGs;
import com.trustworthy.kerberosapi.Models.IncidentPOIs;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ResultPOIs extends Activity {
	
	TextView id_t,type_t,location_t,name_t,secretlevel_t, hellouser_pois;
	String usery;
	int type_num;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.result_pois);
		setContentView(R.layout.result_pois);
		//ListView listView = (ListView)findViewById(R.id.result_pois)
		
		id_t = (TextView)findViewById(R.id.textid);
		type_t = (TextView)findViewById(R.id.texttype);
		location_t = (TextView)findViewById(R.id.textlocation);
		name_t = (TextView)findViewById(R.id.textname);
		secretlevel_t=(TextView)findViewById(R.id.textsecretlevel);
		hellouser_pois=(TextView)findViewById(R.id.hellouser_pois);
		
		Intent intent = getIntent();
		Bundle bundle=intent.getExtras();
        	
		usery = bundle.getString("user");
		
		hellouser_pois.setText(usery);
		
        List<IncidentPOIs> pois= (List<IncidentPOIs>) bundle.getSerializable("pois");

		ListView listView = (ListView) findViewById(R.id.list_pois);
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		for (int i = 0; i < pois.size(); i++) {
			IncidentPOIs poi = pois.get(i);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id_t", poi.getId());
			if (poi.getType() == 0){
				map.put("type_t", "Landmarks");
			}
			if (poi.getType() == 1){
				map.put("type_t", "Victim");
			}
			if (poi.getType() == 2){
				map.put("type_t", "Responder");
			}
			if (poi.getType() == 3){
				map.put("type_t", "Commander");
			}
			map.put("name_t", poi.getName());
			map.put("location_t", poi.getLocation());
			map.put("secretlevel_t", poi.getSecretLevel());
			list.add(map);
			
		}   
		
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.listview_item_pois, new String[]{"id_t", "type_t", "location_t", 
			"name_t", "secretlevel_t"}, new int [] {R.id.textid, R.id.texttype, R.id.textlocation, R.id.textname, R.id.textsecretlevel});
		
		listView.setAdapter(adapter);
		
		
        	
        	
        	//for(int i=0;i<List_id_pois.size();i++){
        		//id=id+List_id_pois.get(i);
        		//type=type+List_type_pois.get(i);
        		//location=location+List_location_pois.get(i);
        		//name=name+List_name_pois.get(i);
        		//secretlevel=secretlevel+List_secretlevel_pois.get(i);
	//}
        	    
        	

		
	}
	
	
}

