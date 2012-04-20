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

public class ScoreView2 extends Activity
{
	TableLayout tl;
	GameScore scores;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_view);
		
		//get handle to the table
		tl = (TableLayout) findViewById(R.id.tbl_scores);
		
		//initialize variable
		scores = new GameScore(getChosenNames());
		scores.addNewRound();
		paintNewRounds();
	}
	
	private void paintNewRounds(){
		TableRow tRow = new TableRow(this);
		
		TextView rt = new TextView(this);
		rt.setText("Round " + scores.currentRound);
		rt.setTextSize(30);
		rt.setGravity(Gravity.CENTER);
		tRow.addView(rt);

		for (int i = 0; i < scores.numPlayers; i++)
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
	
	private void printNewRow()
	{
		TableRow tr = new TableRow(this);
		
		TextView rt = new TextView(this);
		rt.setText("Round " + scores.currentRound);
		rt.setTextSize(30);
		rt.setGravity(Gravity.CENTER);
		tr.addView(rt);
		
		for (int i = 0; i < scores.numPlayers; i++)
		{
			EditText et = new EditText(this);
			et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
			tr.addView(et);
			if (i == 0)
			{
				et.requestFocus();
			}
		}
		//add this row to the table
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		scores.currentRound++;
	}
	
	private static Integer getNumFromET(EditText et){
		Integer returnValue = 0;
		
		String s = et.getText().toString();
		
		try{
			returnValue = Integer.parseInt(s);
		} catch(Exception e){
			
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
					printNewRow();
				}
			});
		return b;
	}
	
	private Button getTotalsButton(){
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
		Button b = new Button(this);
		b.setText("See Totals");
		b.setLayoutParams(p);
		b.setOnClickListener(new View.OnClickListener(){
				public void onClick(View p1)
				{
					onSubmitClick();
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
//		for (PlayerScore ps : scores.ps)
//		{
//			tr.addView(getNameTV(ps.name));
//		}
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
	
	public void onSubmitClick()
	{
		//clear the current scores (since we re-calculate it here)
		clearPlayerScores();
		
		TableLayout tl = (TableLayout)findViewById(R.id.tbl_scores);
		TableRow tr;
		EditText et;
		String text;
		Integer sc;
		for (int i = 1; i < tl.getChildCount(); i++)
		{
			tr = (TableRow)tl.getChildAt(i);
			for (int j = 1; j < tr.getChildCount(); j++)
			{
				et = (EditText)tr.getChildAt(j);
				sc = getNumFromET(et);
				//add that number to this player
//				scores.ps.get(j - 1).scores.add(new RoundScore());
				//scores.ps.get(j - 1).scores.(sc);
			}
		}
		showScores();
	}
	
	private void clearPlayerScores(){
		/*
		for (int y = 0; y < scores.ps.size(); y++)
		{
			scores.ps.get(y).scores.clear();
		}
		*/
	}
	
	private void showScores()
	{
		String output = "";
		/*
		for (PlayerScore ps:scores.ps)
		{
			int pTotal = 0;
			output += ps.name + ": ";
			for (RoundScore x:ps.scores)
			{
				pTotal += x.score;
			}
			output += pTotal + "\n";
		}
		*/
		output += "\nDo you want to play a new game?";
		
		AlertDialog.Builder build = new AlertDialog.Builder(this);
		build.setMessage(output)
		.setPositiveButton("New", new AlertDialog.OnClickListener(){
			public void onClick(DialogInterface p1, int p2)
			{
				//new
				clearPlayerScores();
				scores.currentRound = 1;
				tl.removeAllViews();
				printNames();
				printNewRow();
				
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
