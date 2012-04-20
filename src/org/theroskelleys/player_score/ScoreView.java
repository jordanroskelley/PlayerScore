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
	
	/**
	 * Creates a new TableRow to add to the TableLayout
	 */
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
	
	/**
	 * Creates a 'New Round' button that creates a new TableRow in which to enter scores
	 * @return - a Button view
	 */
	private Button getNewRoundButton(){
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
		Button b = new Button(this);
		b.setText("New");
		b.setLayoutParams(p);
		b.setOnClickListener(new View.OnClickListener(){
				public void onClick(View p1)
				{
					addRound();
				}
			});
		return b;
	}
	
	/**
	 * Creates a 'Totals' button, that calculates totals, and can allow to end the game 
	 * @return - a Button View
	 */
	private Button getTotalsButton(){
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
		Button b = new Button(this);
		b.setText("Totals");
		b.setLayoutParams(p);
		b.setOnClickListener(new View.OnClickListener(){
				public void onClick(View p1)
				{
					finishGame();
				}
			});
		return b;
	}
	
	/**
	 * Sets up a new TextView with a given name pre-filled in it
	 * @param name - the name to put in the TextView
	 * @return - the actual, newly instantiated TextView
	 */
	private TextView getNameTV(String name){
		TextView tv = new TextView(this);
		tv.setText(name);
		tv.setTextSize(25);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}
    
	/**
	 * gets an integer from out of an EditText
	 * @param et - the EditText to check
	 * @return - the number found in the EditText
	 */
    private static Integer getNumFromET(EditText et){
        int returnValue;
        
        String s = et.getText().toString();
        
        try{
            returnValue = Integer.parseInt(s);
        } catch(Exception e){
            returnValue = 0;
        }
        
        return returnValue;
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
	 *  
	 */
	
	/**
	 * Looks up the names of the players for this round
	 * @return - an array of the names of the players chosen in the previous Activity
	 */
	private String[] getChosenNames()
	{
		SharedPreferences sp = PreferenceManager
			.getDefaultSharedPreferences(this);
		String jsonChosenNames = sp.getString("chosenNames", "");
		Gson g = new Gson();
		return g.fromJson(jsonChosenNames, String[].class);
		
	}
	
	/**
	 * Adds up the totals, and then calls showScores()
	 */
	public void finishGame()
	{
		TableRow tr;
		EditText et;
		int sc;
		totals = new int[players.length];
		for(int x = 0; x < totals.length; x++){
			totals[x] = 0;
		}
		
		for (int i = 1; i <= round; i++)
		{
			tr = (TableRow)tl.getChildAt(i);
			for (int j = 0; j < players.length; j++)
			{
				et = (EditText)tr.getChildAt(j+1);
				sc = getNumFromET(et);
				totals[j] += sc;
			}
		}
		showScores();
	}
	
	/**
	 * Creates a dialog to show the scores, and allow the user to choose whether to continue, quit, or start a new game
	 */
	private void showScores()
	{
		StringBuilder output = new StringBuilder();
		output.append("Scores");
		for (int y = 0; y < players.length; y++)
		{
			output.append("\n").append(players[y]).append(": ").append(totals[y]);
		}
		output.append("\n\nDo you want to play a new game?");
		
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
