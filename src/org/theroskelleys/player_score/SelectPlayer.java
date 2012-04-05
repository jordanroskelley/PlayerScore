package org.theroskelleys.player_score;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class SelectPlayer extends ListActivity {
	
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		
		// Create an array of Strings, that will be put to our ListActivity
		ArrayAdapter<CheckRowItem> adapter = new CheckArrayAdapter(this,
				getModel());
		
		setListAdapter(adapter);
	}

	private List<CheckRowItem> getModel() {
		List<CheckRowItem> list = new ArrayList<CheckRowItem>();
		list.add(get("Linux"));
		list.add(get("Windows7"));
		list.add(get("Suse"));
		// Initially select one of the items
		list.get(1).setSelected(true);
		return list;
	}

	private CheckRowItem get(String s) {
		return new CheckRowItem(s);
	}
}
