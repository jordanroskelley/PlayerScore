package org.theroskelleys.player_score;
import java.util.*;

public class GameScore
{
	int currentRound, numPlayers;
	String [] playerNames;
	ArrayList<Round> rounds;

	public GameScore(String[] playerNames){
		currentRound = 0;
		numPlayers = playerNames.length;
		this.playerNames = playerNames;
		rounds = new ArrayList<Round>();
	}

	public void addNewRound(){
		currentRound++;
		rounds.add(new Round(currentRound, numPlayers));
	}
}
