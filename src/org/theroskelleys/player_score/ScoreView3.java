package org.theroskelleys.player_score;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.view.ViewGroup.*;
import android.view.inputmethod.*;
import android.widget.*;
import com.google.gson.*;

public class ScoreView3 extends Activity
{
	TableLayout tl;
	String[] players;
	int round;
	int[] totals;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_view);
		
		//get handle to the table
		tl = (TableLayout) findViewById(R.id.tbl_scores);
		
		//initialize variable
		players = getChosenNames();
		round = 0;
		printNames();
		addRound();
	}
	
	private void addRound(){
		round++;
		TableRow tRow = new TableRow(this);
		TextView rt = new TextView(this);
		rt.setText("Round " + round);
		rt.setTextSize(30);
		rt.setGravity(Gravity.CENTER);
		tRow.addView(rt);

		for (int i = 0; i < players.length; i++)
		{
			EditText et = new EditText(this);
			et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
			tRow.addView(et);
			if (i == 0)
			{
				et.requestFocus();
			}
		}
		//add this row to the table
		tl.addView(tRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	private static Integer getNumFromET(EditText et){
		int returnValue;
		
		String s = et.getText().toString();
		
		try{
			returnValue = Integer.parseInt(s);
		} catch(Exception e){
			returnVlaue = 0;
		}
		
		return returnValue;
	}
	
	private Button getNewRoundButton(){
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
		Button b = new Button(this);
		b.setText("New Round");
		b.setLayoutParams(p);
		b.setOnClickListener(new View.OnClickListener(){
				public void onClick(View p1)
				{
					addRound();
				}
			});
		return b;
	}]
	
	private Button getTotalsButton(){
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
		Button b = new Button(this);
		b.setText("See Totals");
		b.setLayoutParams(p);
		b.setOnClickListener(new View.OnClickListener(){
				public void onClick(View p1)
				{
					finishGame();
				}
			});
		return b;
	}
	
	private TextView getNameTV(String name){
		TextView tv = new TextView(this);
		tv.setText(name);
		tv.setTextSize(30);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}
	
	/**
	 * Prints the names of the current players to the table
	 */
	private void printNames()
	{
		TableRow tr = new TableRow(this);
		LinearLayout ll_btn = new LinearLayout(this);
		ll_btn.setOrientation(LinearLayout.HORIZONTAL);	
		ll_btn.addView(getNewRoundButton());
		ll_btn.addView(getTotalsButton());
		tr.addView(ll_btn);
		
		//add the names
		for (String name : players)
		{
			tr.addView(getNameTV(name));
		}
		
		//add this row to the table
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	/**
	 * Looks up the names of the players for this round, and creates PlayerScore sub-objects for the GameScore object 
	 */
	private String[] getChosenNames()
	{
		SharedPreferences sp = PreferenceManager
			.getDefaultSharedPreferences(this);
		String jsonChosenNames = sp.getString("chosenNames", "");
		Gson g = new Gson();
		return g.fromJson(jsonChosenNames, String[].class);
		
	}
	
	public void finishGame()
	{
		TableRow tr;
		EditText et;
		String text;
		int sc;
		totals = new int[players.length];
		for(int x = 0; x < totals.length; x++){
			totals[x] = 0;
		}
		
		for (int i = 1; i < round; i++)
		{
			tr = (TableRow)tl.getChildAt(i);
			for (int j = 1; j < players.length; j++)
			{
				et = (EditText)tr.getChildAt(j);
				sc = getNumFromET(et);
				totals[i] += sc;
			}
		}
		showScores();
	}
	
	private void showScores()
	{
		StringBuilder output = new StringBuilder();
		for (int y = 0; y < players.length: y++)
		{
			output.append(players[y]).append(": ").append(totals[y]);
		}
		output.append("\nDo you want to play a new game?");
		
		AlertDialog.Builder build = new AlertDialog.Builder(this);
		build.setMessage(output.toString())
		.setPositiveButton("New", new AlertDialog.OnClickListener(){
			public void onClick(DialogInterface p1, int p2)
			{
				//new
			}
		})
		.setNegativeButton("Quit", new AlertDialog.OnClickListener(){
			public void onClick(DialogInterface p1, int p2)
			{
				//quit
				finish();
			}
		})
		.setNeutralButton("Continue", new AlertDialog.OnClickListener(){
			public void onClick(DialogInterface p1, int p2)
			{
				//nothing...
			}
		});
		build.create().show();
	}
}
