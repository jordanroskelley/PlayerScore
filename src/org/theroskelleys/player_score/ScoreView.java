package org.theroskelleys.player_score;

import com.google.gson.Gson;

import android.app.*;
import android.content.SharedPreferences;
import android.os.*;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreView extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_view);
        
        TableLayout tl = (TableLayout)findViewById(R.id.tbl_scores);
        
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        
        String[] chosenNames = getChosenNames();
        View cornerPlaceholder = new View(this);
        tr.addView(cornerPlaceholder);
        for(String name:chosenNames) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            tv.setText(name);
            tr.addView(tv);
        }
        
        tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        tl.invalidate();
    }
    
    private String[] getChosenNames() {
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    	String jsonChosenNames = sp.getString("chosenNames", "");
        Toast.makeText(this, jsonChosenNames, Toast.LENGTH_SHORT).show();
    	Gson g = new Gson();
    	String[] chosenNames = g.fromJson(jsonChosenNames, String[].class);
    	return chosenNames;
    }
    
    public void buttonHandler(View v){
    	switch(v.getId()){
    	
    	}
    }
}
