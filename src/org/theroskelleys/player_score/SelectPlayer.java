package org.theroskelleys.player_score;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class SelectPlayer extends ListActivity {
	SharedPreferences sp;
	Gson g;
	List<CheckRowItem> list;
	String[] names;
	
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.select_player);
		
		fillHandles();
		//make sure there are names! it will fc
		if(names.length < 1){
			Toast.makeText(this, "You must add players in Settings", Toast.LENGTH_SHORT).show();
		}
		else{
			ArrayAdapter<CheckRowItem> adapter = new CheckArrayAdapter(this, getModel());
			setListAdapter(adapter);
		}
	}
	
	private void fillHandles() {
		list = new ArrayList<CheckRowItem>();
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		g = new Gson();
		String jsonNames = sp.getString("names", "");
		names = g.fromJson(jsonNames, String[].class);
	}

	private List<CheckRowItem> getModel() {
		for(String s:names) {
			list.add(new CheckRowItem(s));
		}
		
		return list;
	}
	
	public void buttonHandler(View v) {
		switch(v.getId()) {
		case R.id.btn_playerSelectDone:
			//serialize list to json
			ArrayList<String> chosenNames = new ArrayList<String>();
			for(CheckRowItem c:list) {
				if(c.isSelected()) {
					chosenNames.add(c.getName());
				}
			}
			
			if(chosenNames.size() < 1) {
				Toast.makeText(v.getContext(), R.string.err_noPlayers, Toast.LENGTH_SHORT).show();
				return;
			}
			
			String jsonNames = g.toJson(chosenNames);
			SharedPreferences.Editor ed = sp.edit();
			ed.putString("chosenNames", jsonNames);
			ed.commit();
			
			Intent i = new Intent(this, ScoreView.class);
			startActivity(i);
			break;
		}
	}
}
