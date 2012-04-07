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
import android.widget.*;

public class ScoreView extends Activity {
	TableLayout tl;
	GameScores scores;
	TableRow thisRound;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_view);

		tl = (TableLayout) findViewById(R.id.tbl_scores);
		scores = new GameScores();
		getChosenNames();
		printNames();
		paintNextRound();
		paintScoreEntryFields();
	}

	public void buttonHandler(View v) {
		switch (v.getId()) {

		}
	}
	
	public void paintNextRound(){
		thisRound = new TableRow(this);
		TextView tv = new TextView(this);
		String roundName = "Round "+String.valueOf(scores.currentRound);
		Toast.makeText(this, roundName, Toast.LENGTH_LONG).show();
		tv.setText(roundName);
		thisRound.addView(tv);
		tl.addView(thisRound, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
													LayoutParams.WRAP_CONTENT));
	}
	
	private void paintScoreEntryFields() {/*
		LinearLayout ll = (LinearLayout)findViewById(R.id.ll_entry);
		View cornerPlaceholder = new View(this);
		ll.addView(cornerPlaceholder);
		
		for(int i = 0; i < scores.ps.size(); i++) {
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
		View cornerPlaceholder = new View(this);
		tr.addView(cornerPlaceholder);
		for (PlayerScore ps : scores.ps) {
			TextView tv = new TextView(this);
			tv.setText(ps.name);
			tr.addView(tv);
		}
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
	}

	private void getChosenNames() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		String jsonChosenNames = sp.getString("chosenNames", "");
		Gson g = new Gson();
		String[] chosenNames = g.fromJson(jsonChosenNames, String[].class);
		for(String name:chosenNames){
			scores.ps.add(new PlayerScore(name));
		}
	}
}
