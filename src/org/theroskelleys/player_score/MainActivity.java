package org.theroskelleys.player_score;

import android.app.*;
import android.content.Intent;
import android.os.*;
import android.view.View;
import android.content.*;
import android.preference.*;
import com.google.gson.*;
import android.widget.*;
import android.util.*;

public class MainActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void buttonHandler(View v){
    	Intent i;
    	switch(v.getId()){
    	case R.id.btn_start:
			//check for empty list here before we go on
			String[] vals = getAllNames(this);
			if(vals == null || vals.length < 1){
				Toast.makeText(this, "You need to set up some names in Settings first.", Toast.LENGTH_LONG).show();
			}
			else{
				i = new Intent(this, SelectPlayer.class);
				startActivity(i);
			}
    		break;
    	case R.id.btn_settings:
			i = new Intent(this, Settings.class);
			startActivity(i);
    		break;
		case R.id.btn_load:
			GameState[] games = getSavedGames(this);
			if(games.length == 0){
				Toast.makeText(getApplicationContext(), "You have no saved games.", Toast.LENGTH_LONG).show();
			} else {
				String[] gNames = new String[games.length];
				for(int c = 0; c < games.length; c++){
					gNames[c] = games[c].getName();
				}
				
				AlertDialog.Builder b = new AlertDialog.Builder(this);
				b.setTitle("Which to load:")
				.setItems(gNames, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						//store that number, we will load that game
						Intent intent = new Intent(getApplicationContext(), ScoreView.class);
						intent.putExtra("sgn", item);
						startActivity(intent);
					}
				});
				b.create().show();
			}
			break;
		}
    }
	
	public static GameState[] getSavedGames(Context ctx){
		GameState[] retVal;
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
		Gson g = new Gson();
		String jsonValues = sp.getString("games", null);
		if(jsonValues == null){
			retVal = new GameState[0];
		} else {
			retVal = g.fromJson(jsonValues, GameState[].class);
		}
		return retVal;
	}
	
	public static String[] getAllNames(Context ctx){
		String[] retVal;
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
		Gson g = new Gson();
		String jsonValues = sp.getString("names", "");
		retVal = g.fromJson(jsonValues, String[].class);
		return retVal;
	}
}
