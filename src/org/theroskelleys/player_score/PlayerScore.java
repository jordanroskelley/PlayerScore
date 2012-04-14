package org.theroskelleys.player_score;
import java.util.*;
import android.widget.*;

public class PlayerScore
{
	String name;
	ArrayList<Integer> scores;
	
	public PlayerScore(String name){
		this.name = name;
		scores = new ArrayList<Integer>();
	}
}
