package org.theroskelleys.player_score;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

public class ScoreView extends Activity {
	TableLayout tl;
	int roundNumber, numPlayers;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_view);

		tl = (TableLayout) findViewById(R.id.tbl_scores);
		printNames();
		paintScoreEntryFields();
		roundNumber = 1;
	}

	/*
	 * What to do... Have array of objects to represent the table, and repaint
	 * between entries? - not efficient...
	 */

	public void buttonHandler(View v) {
		switch (v.getId()) {

		}
	}
	
	private void paintScoreEntryFields() {/*
		LinearLayout ll = (LinearLayout)findViewById(R.id.ll_entry);
		View cornerPlaceholder = new View(this);
		ll.addView(cornerPlaceholder);
		
		for(int i = 0; i < numPlayers; i++) {
			//add entry
			EditText et = new EditText(this);
			//TODO possibly hold an array of et's in memory, and then when they finish a round, just loop that array?
			et.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			ll.addView(et);
		}
		*/
	}

	private void printNames() {
		TableRow tr = new TableRow(this);
		String[] chosenNames = getChosenNames();
		numPlayers = chosenNames.length;
		View cornerPlaceholder = new View(this);
		tr.addView(cornerPlaceholder);
		for (String name : chosenNames) {
			TextView tv = new TextView(this);
			tv.setText(name);
			tr.addView(tv);
		}
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
	}

	private String[] getChosenNames() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		String jsonChosenNames = sp.getString("chosenNames", "");
		Gson g = new Gson();
		String[] chosenNames = g.fromJson(jsonChosenNames, String[].class);
		return chosenNames;
	}
}
