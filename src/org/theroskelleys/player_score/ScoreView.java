package org.theroskelleys.player_score;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import android.view.*;
import android.widget.*;

public class ScoreView extends Activity {
	private static final String TAG = "ScoreView";
	private static final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1.0f);
	//private LinearLayout list;
	TableLayout tl;
	private String[] players;
	private int round;
	private int[] totals;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_view);

		// get handle to the list
		//list = (LinearLayout) findViewById(R.id.ll_scores);
		tl = (TableLayout) findViewById(R.id.tl_scores);

		// if there was a previous instance, set it up
		GameState previousGame = null;
		String gameStr = null;
		try{
			gameStr = savedInstanceState.getString("GameState");
		} catch(Exception e){}
		
		if(gameStr != null){
			Gson g = new Gson();
			previousGame = g.fromJson(gameStr, GameState.class);
			// set up the current game using the previous state
			setUpGame(previousGame);
		} else {
			// initialize variables for a new game
			initializeNewGame();
		}
		setupTotalsRow();
	}
	
	private TableRow getNewRow(Integer[] roundScores){
		TableRow rb = new TableRow(this);
		round++;
		rb.addView(getScoreTV("Round "+round));
		EditText et;
		for(int i = 0; i < players.length; i++){
			if(roundScores != null && roundScores[i] != null){
				et = getET(roundScores[i].toString());
			} else{
				et = getET();
			}
			rb.addView(et);
		}
		return rb;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu m){
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.menu_score_view, m);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem mi){
		switch(mi.getItemId()){
			case R.id.m_end:
				AlertDialog.Builder build = new AlertDialog.Builder(this);
				build.setMessage("Are you sure you want to quit?")
					.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface p1, int p2) {
							finish();
						}
					})
					.setNegativeButton("No", new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface p1, int p2) {
						}
					});
				build.create().show();
				return true;
			case R.id.m_save:
				
				return true;
			case R.id.m_done:
				calculateTotals();
				showScores();
				return true;
			case R.id.m_total:
				//show/hide totals view
				LinearLayout totalsLL = (LinearLayout)findViewById(R.id.ll_totals);
				if(totalsLL.getVisibility() == View.VISIBLE){
					totalsLL.setVisibility(View.GONE);
				} else{
					totalsLL.setVisibility(View.VISIBLE);
				}
				return true;
			default:
				return super.onOptionsItemSelected(mi);
		}
	}

	/**
	 * sets up some variables for a new game
	 */
	private void initializeNewGame() {
		// initialize variable
		players = getChosenNames();
		totals = new int[players.length];
		for(int i = 0; i < totals.length; i++) {
			totals[i] = 0;
		}
		round = 0;
		printNames();
		TableRow r = getNewRow(null);
		tl.addView(r);
	}

	/**
	 * Takes a previous game state, and paints the names and previous scores
	 * 
	 * @param gs
	 */
	private void setUpGame(GameState gs) {
		players = gs.getPlayers();
		totals = new int[players.length];
		for(int i = 0; i < totals.length; i++) {
			totals[i] = 0;
		}
		Integer[][] tempScores = gs.getRoundScores();

		// add the names row
		printNames();
		
		round = 0;

		for (int i = 0; i < tempScores.length; i++) {
			tl.addView(getNewRow(tempScores[i]));
		}
	}

	/**
	 * This is to help us when the screen rotates
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("GameState", toGameState().toJson());
	}

	/**
	 * Grabs this current game, and packages it up into a GameState object
	 * @return
	 */
	public GameState toGameState() {
		Integer[][] scoreArray = new Integer[round][players.length];

		for (int i = 0; i < tl.getChildCount(); i++) {
			//LinearLayout ll = (LinearLayout) list.getChildAt(i);
			TableRow ll = (TableRow) tl.getChildAt(i);
			
			for (int j = 1; j < ll.getChildCount(); j++) {
				EditText et = (EditText) ll.getChildAt(j);
				Integer s = getNumFromET(et);
				scoreArray[i][j - 1] = s;
			}
		}
		return new GameState(players, scoreArray);
	}

	/**
	 * Creates a 'New Round' button that creates a new TableRow in which to
	 * enter scores
	 * 
	 * @return - a Button view
	 */
	private Button getNewRoundButton() {
		Button b = new Button(this);
		b.setText("New");
		b.setLayoutParams(lp);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View p1) {
				TableRow r = getNewRow(null);
				tl.addView(r);
			}
		});
		return b;
	}

	/**
	 * Sets up a new TextView with a given name pre-filled in it
	 * 
	 * @param name
	 *            - the name to put in the TextView
	 * @return - the actual, newly instantiated TextView
	 */
	private TextView getTV() {
		TextView tv = new TextView(this);
		tv.setLayoutParams(lp);
		tv.setTextSize(25);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}
	
	private TextView getTV(String name){
		TextView tv = getTV();
		tv.setText(name);
		return tv;
	}
	
	private TextView getScoreTV(String name){
		TextView tv = getScoreTV();
		tv.setText(name);
		return tv;
	}

	private TextView getScoreTV() {
		TextView tv = new TextView(this);
		//tv.setLayoutParams(lp);
		tv.setTextSize(15);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}

	private EditText getET(String text) {
		EditText et = getET();
		et.setText(text);
		return et;
	}

	private EditText getET() {
		EditText et = new EditText(this);
		et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
		//et.setLayoutParams(lp);
		et.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					calculateTotals();
					//Toast.makeText(v.getContext(), "lost focus", Toast.LENGTH_SHORT).show();
				}
			}
		});
		return et;
	}

	/**
	 * gets an integer from out of an EditText
	 * 
	 * @param et
	 *            - the EditText to check
	 * @return - the number found in the EditText
	 */
	private static Integer getNumFromET(EditText et) {
		int returnValue;
		String s = et.getText().toString();
		try {
			returnValue = Integer.parseInt(s);
		} catch (Exception e) {
			returnValue = 0;
		}
		return returnValue;
	}

	/**
	 * Prints the names of the current players to the table
	 */
	private void printNames() {
		LinearLayout playerLL = (LinearLayout) findViewById(R.id.ll_players);
		playerLL.addView(getNewRoundButton());

		// add the names
		for (String name : players) {
			playerLL.addView(getTV(name));
		}
	}

	private void setupTotalsRow() {
		LinearLayout totalLL = (LinearLayout) findViewById(R.id.ll_totals);
		TextView titleTV = getTV("Totals");
		totalLL.addView(titleTV);

		for (String name : players) {
			totalLL.addView(getTV());
		}
	}

	/**
	 * Looks up the names of the players for this round
	 * 
	 * @return - an array of the names of the players chosen in the previous
	 *         Activity
	 */
	private String[] getChosenNames() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		String jsonChosenNames = sp.getString("chosenNames", "");
		Gson g = new Gson();
		return g.fromJson(jsonChosenNames, String[].class);

	}

	/**
	 * Adds up the totals, and then calls showScores()
	 */
	public void calculateTotals() {
		LinearLayout scoreRow;
		EditText et;
		int sc;
		for(int x = 0; x < totals.length; x++) {
			totals[x] = 0;
		}

		for (int i = 0; i < tl.getChildCount(); i++) {
			scoreRow = (LinearLayout) tl.getChildAt(i);
			for (int j = 1; j < scoreRow.getChildCount(); j++) {
				et = (EditText) scoreRow.getChildAt(j);
				sc = getNumFromET(et);
				totals[j-1] += sc;
			}
		}
		
		//and now update the totals views
		LinearLayout tot = (LinearLayout)findViewById(R.id.ll_totals);
		for(int i = 1; i < tot.getChildCount(); i++){
			TextView tv = (TextView)tot.getChildAt(i);
			tv.setText(String.valueOf(totals[i-1]));
		}
	}

	/**
	 * Creates a dialog to show the scores, and allow the user to choose whether
	 * to continue, quit, or start a new game
	 */
	private void showScores() {
		StringBuilder output = new StringBuilder();
		output.append("Scores");
		for (int y = 0; y < players.length; y++) {
			output.append("\n").append(players[y]).append(": ")
					.append(totals[y]);
		}
		output.append("\n\nDo you want to play a new game?");

		AlertDialog.Builder build = new AlertDialog.Builder(this);
		build.setMessage(output.toString())
				.setPositiveButton("New", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface p1, int p2) {
						// new
						// TODO figure this out :)
					}
				})
				.setNegativeButton("Quit", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface p1, int p2) {
						// quit
						finish();
					}
				})
				.setNeutralButton("Continue",
						new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface p1, int p2) {
								// nothing...
							}
						});
		build.create().show();
	}
}
