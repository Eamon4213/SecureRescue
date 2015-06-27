package com.trustworthy.kerberosdroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trustworthy.kerberosapi.Models.IncidentMSGs;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ResultMSGs extends Activity {
	
	TextView id_g,title_g,message_g,secretlevel_g, hellouser_msgs;
	String userx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_msgs);
		id_g = (TextView)findViewById(R.id.textidg);
		title_g = (TextView)findViewById(R.id.texttitleg);
		message_g = (TextView)findViewById(R.id.textmessageg);
		secretlevel_g=(TextView)findViewById(R.id.textsecretlevelg);
		hellouser_msgs=(TextView)findViewById(R.id.hellouser_msgs);
		
		Intent intent = getIntent();
		Bundle bundle=intent.getExtras();
		
		userx = bundle.getString("user");
		
		hellouser_msgs.setText(userx);
		
		  List<IncidentMSGs> msgs= (List<IncidentMSGs>) bundle.getSerializable("msgs");

			ListView listView = (ListView) findViewById(R.id.list_msgs);
			
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			
			for (int j = 0; j < msgs.size(); j++) {
				IncidentMSGs msg = msgs.get(j);
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id_g", msg.getId());
				map.put("title_g", msg.getTitle());
				map.put("message_g", msg.getMessage());
				map.put("secretlevel_g", msg.getSecretLevel());
				list.add(map);
				
			}   
			
			SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.listview_item_msgs, new String[]{"id_g", "title_g", "message_g", 
				"secretlevel_g"}, new int [] {R.id.textidg, R.id.texttitleg, R.id.textmessageg, R.id.textsecretlevelg});
					
			listView.setAdapter(adapter);
	
	}
        	
		
	}


