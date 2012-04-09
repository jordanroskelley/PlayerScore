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
		//initialize variable
		scores = new GameScores();
		//load names from shared prefs into scores
		getChosenNames();
		//print the row with the names (and a blank above the round column)
		//printNames();
		//create a new row and add the round information
		paintNextRound();
		paintNextRound();
		//should draw the correct number of edittexts into the ll
		//paintScoreEntryFields();
	}
	
	public void paintNextRound(){
		thisRound = new TableRow(this);
		
		TextView tv = new TextView(this);
		tv.setText("Round "+String.valueOf(scores.currentRound));
		thisRound.addView(tv);
		
		for(PlayerScore ps:scores.ps){
			tv = new TextView(this);
			tv.setText(String.valueOf(ps.scores.get(scores.currentRound)));
			thisRound.addView(tv);
		}
		
		scores.currentRound++;
		tl.addView(thisRound, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
													LayoutParams.WRAP_CONTENT));
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
	
	private void paintScoreEntryFields() {
		TableRow ll = (TableRow)findViewById(R.id.tr_entry);
		
		for(int i = 0; i < scores.ps.size(); i++) {
			//add entry
			EditText et = new EditText(this);
			//TODO possibly hold an array of et's in memory, and then when they finish a round, just loop that array?
			et.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			ll.addView(et);
		}
	}
	
	public void buttonHandler(View v) {
		switch (v.getId()) {
			case R.id.btn_submit:
				TableRow ets = (TableRow)findViewById(R.id.tr_entry);
				ets.getChildAt(1);
				break;
		}
	}
}
