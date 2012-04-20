package org.theroskelleys.player_score;

public class Round
{
	int roundNumber;
	int[] roundScores;
	
	public Round(int roundNumber, int numPlayers){
		this.roundNumber = roundNumber;
		roundScores = new int[numPlayers];
	}
	
	public String getRoundName(){
		return "Round " + roundNumber;
	}
}
