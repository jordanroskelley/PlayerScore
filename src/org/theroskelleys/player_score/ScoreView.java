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
		paintScoreEntryFields();
		redrawScores();
		//print the row with the names (and a blank above the round column)
		//printNames();
		//create a new row and add the round information
		//paintNextRound();
		//should draw the correct number of edittexts into the ll
		//paintScoreEntryFields();
	}
	
	/*
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
		tl.addView(thisRound, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	*/
	
	/**
	 * redraws the table, called between each score entry to keep the UI up to date
	 */
	private void redrawScores() {
		//remove old data and redraw the names
		tl.removeAllViews();
		printNames();
		
		//roundCounter is just the counter to move through the rounds, scores.currentRound is the current round being played. this keeps us from printing rounds ahead of the current round
		for(int roundCounter = 0; roundCounter < scores.currentRound; roundCounter++) {
			//get a new row
			thisRound = new TableRow(this);
			
			//set the round name column
			TextView tv = new TextView(this);
			tv.setText("Round "+String.valueOf(scores.currentRound));
			thisRound.addView(tv);
			
			//for each player, look up their score for the current round and add it to the row
			for(PlayerScore ps:scores.ps){
				tv = new TextView(this);
				String setText;
				
				try {
					setText = String.valueOf(ps.scores.get(roundCounter));
				}
				catch(Exception e) {
					setText = "0";
				}
				
				tv.setText(String.valueOf(setText));
				thisRound.addView(tv);
			}
			//add the row, and move on to the next round, until you hit the current round
			tl.addView(thisRound, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}

	/**
	 * Prints the names of the current players to the table
	 */
	private void printNames() {
		TableRow tr = new TableRow(this);
		//add a blank view to the corner, to go above the round counts...
		tr.addView(new TextView(this));
		//add the names
		for (PlayerScore ps : scores.ps) {
			TextView tv = new TextView(this);
			tv.setText(ps.name);
			tr.addView(tv);
		}
		//add this row to the table
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	/**
	 * Looks up the names of the players for this round, and creates PlayerScore sub-objects for the GameScore object 
	 */
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
	
	/**
	 * We have to do this dynamically instead of in xml because we don't know how many players there will be... we have already painted the submit button though
	 */
	private void paintScoreEntryFields() {
		//TableRow entryRow = (TableRow)findViewById(R.id.tr_entry);
		LinearLayout llEntry = (LinearLayout)findViewById(R.id.ll_entry);
		
		for(int i = 0; i < scores.ps.size(); i++) {
			//add entry
			EditText et = new EditText(this);
			//TODO possibly hold an array of et's in memory, and then when they finish a round, just loop that array?
			et.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			et.setWidth(300);
			llEntry.addView(et);
			//entryRow.addView(et);
		}
	}
	
	/**
	 * Handles button views, pretty basic
	 * @param v - the view that was clicked, we check it's id to see who it was
	 */
	public void buttonHandler(View v) {
		switch (v.getId()) {
			case R.id.btn_submit:
				//get handle to row
				//TableRow row = (TableRow)findViewById(R.id.tr_entry);
				LinearLayout llEntry = (LinearLayout)findViewById(R.id.ll_entry);
				
				EditText et;
				//for each child (excepting the submit button) get it's text and if it isn't null/empty, update this round's scores with that number
				int childrenNum = (llEntry.getChildCount()-1);
				Toast.makeText(this, "children: "+childrenNum, Toast.LENGTH_LONG).show();
				for(int i = 1; i < childrenNum; i++) {
					et = (EditText)llEntry.getChildAt(i);
					String entered = et.getText().toString();
					
					if(entered != null && entered.length() > 0) {
						//update scores
						//TODO this gives a number parse error...
						scores.ps.get(i).scores.set(scores.currentRound, Integer.parseInt(entered));
					}
				}
				break;
		}
	}
}
