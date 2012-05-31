package org.theroskelleys.player_score;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import android.view.*;
import android.widget.*;
import java.util.*;

public class ScoreView extends Activity {
	private static final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1.0f);
	TableLayout tl;
	private String[] players;
	private int round;
	private int[] totals;
	private GameState thisGame;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_view);

		// get handle to the list
		tl = (TableLayout) findViewById(R.id.tl_scores);
		
		String gString = null;
		try{
			if(savedInstanceState != null){
				gString = savedInstanceState.getString("GameState", null);
			} else {
				gString = getIntent().getExtras().getString("GameState");
			}
		} catch(Exception e){
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		if(gString != null){
			//load game
			Gson g = new Gson();
			thisGame = g.fromJson(gString, GameState.class);
			setUpGame(thisGame);
		} else {
			//initialize new game
			initializeNewGame();
		}
		setupTotalsRow();
		calculateTotals();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		try{
			String gstate = toGameState().toJson();
			outState.putString("GameState", gstate);
		} catch(Exception e){
			Toast.makeText(this, "Error saving state", Toast.LENGTH_LONG).show();
		}
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		//set up stuff, and detect size...
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
				saveGame();
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
					calculateTotals();
				}
				return true;
			default:
				return super.onOptionsItemSelected(mi);
		}
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
			if(i == 0){
				et.requestFocus();
			}
			rb.addView(et);
		}
		return rb;
	}
	
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
	
	private void setUpGame(GameState gs) {
		if(gs.getIsTotalShowing()){
			LinearLayout ll = (LinearLayout)findViewById(R.id.ll_totals);
			ll.setVisibility(View.VISIBLE);
		}
		
		if(gs.getName() != null){
			setTitle(getResources().getString(R.string.app_name)+": " + gs.getName());
		}
		
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
	
	public GameState toGameState() {
		//get the scores
		Integer[][] scoreArray = new Integer[round][players.length];
		for (int i = 0; i < tl.getChildCount(); i++) {
			TableRow row = (TableRow) tl.getChildAt(i);
			
			for (int j = 1; j < row.getChildCount(); j++) {
				EditText et = (EditText) row.getChildAt(j);
				Integer s = getNumFromET(et);
				scoreArray[i][j - 1] = s;
			}
		}
		
		//get whether or not the totals is showing
		Boolean b;
		LinearLayout totView = (LinearLayout)findViewById(R.id.ll_totals);
		if(totView.getVisibility() == View.GONE){
			b = false;
		} else {
			b = true;
		}
		
		String gameName = null;
		if(thisGame != null){
			gameName = thisGame.getName();
		}
		
		return new GameState(players, scoreArray, b, gameName);
	}
	
	private void saveGame(){
		thisGame = toGameState();
		if(thisGame.getName() == null){
			//get name
			final EditText nameET = new EditText(this);
			
			AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setMessage("Please enter a save name:")
				.setView(nameET)
				.setPositiveButton("OK", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface p1, int p2) {
						String name = nameET.getText().toString();
						thisGame.setName(name);
						setTitle(getResources().getString(R.string.app_name) + ": " + name);
						finishSaving();
					}
				})
				.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface p1, int p2) {}
				});
			build.create().show();
		} else{
			finishSaving();
		}
	}
	
	private void finishSaving(){
		//now, de-serialize the current saved games array, add thisGame, and re-serialize it
		//get old list. if it is null, make new. otherwise, read in old, add this, re-serialize, and re-commit
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String gamesString = sp.getString("games", null);
		Gson g = new Gson();
		ArrayList<GameState> games = new ArrayList<GameState>();
		
		if(gamesString != null) {
			GameState[] arrGames = g.fromJson(gamesString, GameState[].class);
			games = new ArrayList<GameState>(Arrays.asList(arrGames));
		}
		boolean isAdded = false;
		for(int i = 0; i < games.size(); i++){
			if(thisGame.getName().equals(games.get(i).getName())){
				games.set(i, thisGame);
				isAdded = true;
			}
		}
		if(!isAdded){
			games.add(thisGame);
		}
		
		String out = g.toJson(games);
		SharedPreferences.Editor ed = sp.edit();
		ed.putString("games", out);
		ed.commit();
		Toast.makeText(this, "Game "+thisGame.getName()+" saved", Toast.LENGTH_SHORT).show();
	}
	
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
		tv.setTextSize(20);
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
		et.setOnKeyListener(new EditText.OnKeyListener(){
				public boolean onKey(View p1, int p2, KeyEvent p3)
				{
					if(p3.getKeyCode() == KeyEvent.KEYCODE_0 || 
					   p3.getKeyCode() == KeyEvent.KEYCODE_1 || 
					   p3.getKeyCode() == KeyEvent.KEYCODE_2 || 
					   p3.getKeyCode() == KeyEvent.KEYCODE_3 || 
					   p3.getKeyCode() == KeyEvent.KEYCODE_4 || 
					   p3.getKeyCode() == KeyEvent.KEYCODE_5 || 
					   p3.getKeyCode() == KeyEvent.KEYCODE_6 || 
					   p3.getKeyCode() == KeyEvent.KEYCODE_7 || 
					   p3.getKeyCode() == KeyEvent.KEYCODE_8 || 
					   p3.getKeyCode() == KeyEvent.KEYCODE_9 || 
					   p3.getKeyCode() == KeyEvent.KEYCODE_DEL
					){
						calculateTotals();
					}
					return false;
				}
		});
		return et;
	}
	
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

		for (int i = 0; i < players.length; i++) {
			totalLL.addView(getTV());
		}
	}
	
	private String[] getChosenNames() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		String jsonChosenNames = sp.getString("chosenNames", "");
		Gson g = new Gson();
		return g.fromJson(jsonChosenNames, String[].class);

	}
	
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
						//TODO remove current save from saves
						tl.removeAllViews();
						round = 0;
						TableRow row = getNewRow(null);
						tl.addView(row);
						thisGame = null;
						calculateTotals();
					}
				})
				.setNegativeButton("Quit", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface p1, int p2) {
						//TODO maybe ask if they want current removed...
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
