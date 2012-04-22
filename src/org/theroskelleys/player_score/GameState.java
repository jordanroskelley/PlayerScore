package org.theroskelleys.player_score;

public class GameState
{
	private String[] players;
	private int[][] roundScores;
	
	public GameState(String[] players, int[][] roundScores){
		this.players = players;
		this.roundScores = roundScores;
	}
}
