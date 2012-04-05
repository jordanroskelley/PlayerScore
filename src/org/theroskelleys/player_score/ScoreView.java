package org.theroskelleys.player_score;

import android.app.*;
import android.content.Intent;
import android.os.*;
import android.view.View;

public class ScoreView extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_view);
    }
    
    public void buttonHandler(View v){
    	Intent i;
    	switch(v.getId()){
    	case R.id.btn_start:
			i = new Intent(this, SelectPlayer.class);
			startActivity(i);
    		break;
    	case R.id.btn_settings:
			i = new Intent(this, Settings.class);
			startActivity(i);
    		break;
    	}
    }
}
