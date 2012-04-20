package org.theroskelleys.player_score;
import java.util.*;

public class GameScores
{
	int currentRound;
	ArrayList<PlayerScore> ps;
	ArrayList<Round> rounds;
	
	public GameScores(){
		currentRound = 1;
		ps = new ArrayList<PlayerScore>();
		rounds = new ArrayList<Round>();
	}
	
	public void addNewRound(){
		for(PlayerScore p:ps){
			p.scores.add(new RoundScore());
		}
	}
}
