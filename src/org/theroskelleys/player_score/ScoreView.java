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

public class ScoreView extends Activity
{
	TableLayout tl;
	GameScores scores;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_view);
		
		tl = (TableLayout) findViewById(R.id.tbl_scores);
		//initialize variable
		scores = new GameScores();
		//load names from shared prefs into scores
		getChosenNames();
		printNames();
		printNewRow();
	}
	
	private void printNewRow()
	{
		TableRow tr = new TableRow(this);
		
		TextView rt = new TextView(this);
		rt.setText("Round " + scores.currentRound);
		rt.setTextSize(30);
		rt.setGravity(Gravity.CENTER);
		tr.addView(rt);
		
		for (int i = 0; i < scores.ps.size(); i++)
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
	
	/**
	 * Prints the names of the current players to the table
	 */
	private void printNames()
	{
		TableRow tr = new TableRow(this);
		LinearLayout ll_btn = new LinearLayout(this);
		ll_btn.setOrientation(LinearLayout.HORIZONTAL);
		
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
		
		Button b = new Button(this);
		b.setText("Add");
		b.setLayoutParams(p);
		b.setOnClickListener(new View.OnClickListener(){
				public void onClick(View p1)
				{
					printNewRow();
				}
			});
		ll_btn.addView(b);
		
		b = new Button(this);
		b.setText("Submit");
		b.setLayoutParams(p);
		b.setOnClickListener(new View.OnClickListener(){
				public void onClick(View p1)
				{
					onSubmitClick();
				}
			});
		ll_btn.addView(b);
		tr.addView(ll_btn);
		
		//add the names
		for (PlayerScore ps : scores.ps)
		{
			TextView tv = new TextView(this);
			tv.setText(ps.name);
			tv.setTextSize(30);
			tv.setGravity(Gravity.CENTER);
			tr.addView(tv);
		}
		//add this row to the table
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	/**
	 * Looks up the names of the players for this round, and creates PlayerScore sub-objects for the GameScore object 
	 */
	private void getChosenNames()
	{
		SharedPreferences sp = PreferenceManager
			.getDefaultSharedPreferences(this);
		String jsonChosenNames = sp.getString("chosenNames", "");
		Gson g = new Gson();
		String[] chosenNames = g.fromJson(jsonChosenNames, String[].class);
		for (String name:chosenNames)
		{
			scores.ps.add(new PlayerScore(name));
		}
	}
	
	public void onSubmitClick()
	{
		//clear the current scores (since we re-calculate it here)
		for (int y = 0; y < scores.ps.size(); y++)
		{
			scores.ps.get(y).scores.clear();
		}
		
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
				//parse out the number
				text = et.getText().toString();
				try
				{
					sc = Integer.parseInt(text);
				}
				catch (Exception e)
				{
					sc = 0;
				}
				
				//then add that number to this player
				scores.ps.get(j - 1).scores.add(sc);
			}
		}
		showScores();
	}
	
	private void showScores()
	{
		String output = "";
		for (PlayerScore ps:scores.ps)
		{
			int pTotal = 0;
			output += ps.name + ": ";
			for (int x:ps.scores)
			{
				pTotal += x;
			}
			output += pTotal + "\n";
		}
		Toast.makeText(this, output, Toast.LENGTH_LONG).show();
	}
}
