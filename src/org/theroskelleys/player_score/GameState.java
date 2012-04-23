package org.theroskelleys.player_score;

import com.google.gson.Gson;

public class GameState
{
	private String[] players;
	private int[][] roundScores;
	
	public GameState(String[] players, int[][] roundScores){
		this.players = players;
		this.roundScores = roundScores;
	}
	
	public String[] getPlayers() {
		return players;
	}
	public void setPlayers(String[] players) {
		this.players = players;
	}
	
	public int[][] getRoundScores(){
		return roundScores;
	}
	public void setRoundScores(int[][] roundScores) {
		this.roundScores = roundScores;
	}
	
	public String toJson(){
		Gson g = new Gson();
		String output = g.toJson(this);
		return output;
	}
}
