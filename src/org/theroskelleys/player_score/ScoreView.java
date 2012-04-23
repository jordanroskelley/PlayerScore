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

public class ScoreView extends Activity {
	private static final String TAG = "ScoreView";
	private static final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1.0f);
	private LinearLayout list;
	private String[] players;
	private int round;
	private int[] totals;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_view);

		// get handle to the list
		list = (LinearLayout) findViewById(R.id.ll_scores);

		// if there was a previous instance, set it up
		GameState previousGame = null;
		try {
			String gameStr = savedInstanceState.getString("GameState");
			Gson g = new Gson();
			previousGame = g.fromJson(gameStr, GameState.class);
		} catch (Exception e) {
			Log.e(TAG, "Exception: error deserializing game state.");
		}

		if (previousGame != null) {
			// set up the current game using the previous state
			setUpGame(previousGame);
		} else {
			// initialize variables for a new game
			initializeNewGame();
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
		addRound();
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
		int[][] tempScores = gs.getRoundScores();

		// add the names row
		printNames();

		for (int i = 0; i < tempScores.length; i++) {
			LinearLayout ll = getLL();
			ll.addView(getTV("Round " + (i + 1)));
			for (int j = 0; j < tempScores[0].length; j++) {
				EditText et = getET(String.valueOf(tempScores[i][j]));
				ll.addView(et);
			}
			list.addView(ll);
			round = i + 1;
		}
	}

	private LinearLayout getLL() {
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		return ll;
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
		int[][] scoreArray = new int[round][players.length];

		for (int i = 0; i < list.getChildCount(); i++) {
			LinearLayout ll = (LinearLayout) list.getChildAt(i);
			for (int j = 1; j < ll.getChildCount(); j++) {
				EditText et = (EditText) ll.getChildAt(j);
				int s = getNumFromET(et);
				scoreArray[i][j - 1] = s;
			}
		}
		return new GameState(players, scoreArray);
	}

	/**
	 * Creates a new TableRow to add to the TableLayout
	 */
	private void addRound() {
		round++;
		LinearLayout ll = getLL();
		TextView tv = getTV("Round " + round);
		ll.addView(tv);

		for (int i = 0; i < players.length; i++) {
			EditText et = getET();
			ll.addView(et);
			if (i == 0) {
				et.requestFocus();
			}
		}
		// add this row to the table
		list.addView(ll);
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
				addRound();
			}
		});
		return b;
	}

	/**
	 * Creates a 'Totals' button, that calculates totals, and can allow to end
	 * the game
	 * 
	 * @return - a Button View
	 */
	private Button getTotalsButton() {
		Button b = new Button(this);
		b.setText("Totals");
		b.setLayoutParams(lp);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View p1) {
				finishGame();
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
	private TextView getTV(String name) {
		TextView tv = new TextView(this);
		tv.setLayoutParams(lp);
		tv.setText(name);
		tv.setTextSize(25);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}

	private TextView getScoreTV() {
		TextView tv = new TextView(this);
		tv.setLayoutParams(lp);
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
		et.setLayoutParams(lp);
		et.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					Log.d(TAG, "view just lost focus :)");
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
		LinearLayout ll_btn = new LinearLayout(this);
		ll_btn.setOrientation(LinearLayout.HORIZONTAL);
		ll_btn.setLayoutParams(lp);
		ll_btn.addView(getNewRoundButton());
		ll_btn.addView(getTotalsButton());
		playerLL.addView(ll_btn);

		// add the names
		for (String name : players) {
			playerLL.addView(getTV(name));
		}
	}

	private void setupTotalsRow() {
		LinearLayout totalLL = (LinearLayout) findViewById(R.id.ll_totals);
		totalLL.addView(new View(this));

		// add the names
		for (String name : players) {
			totalLL.addView(getScoreTV());
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
	public void finishGame() {
		LinearLayout scoreRow;
		EditText et;
		int sc;
		for(int x = 0; x < totals.length; x++) {
			totals[x] = 0;
		}

		for (int i = 0; i < list.getChildCount(); i++) {
			scoreRow = (LinearLayout) list.getChildAt(i);
			for (int j = 1; j < scoreRow.getChildCount(); j++) {
				et = (EditText) scoreRow.getChildAt(j);
				sc = getNumFromET(et);
				totals[j-1] += sc;
			}
		}
		showScores();
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
