package org.theroskelleys.player_score;
import java.util.*;

public class GameScores
{
	int currentRound;
	ArrayList<PlayerScore> ps;
	
	public GameScores(){
		currentRound = 1;
		ps = new ArrayList<PlayerScore>();
	}
}
