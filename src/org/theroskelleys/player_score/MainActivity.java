package org.theroskelleys.player_score;

import android.app.*;
import android.content.Intent;
import android.os.*;
import android.view.View;
import android.content.*;
import android.preference.*;
import com.google.gson.*;
import android.widget.*;

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
    	}
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
