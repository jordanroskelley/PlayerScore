package org.theroskelleys.player_score;

import com.google.gson.Gson;

public class GameState
{
	private String[] players;
	private Integer[][] roundScores;
	
	public GameState(String[] players, Integer[][] roundScores){
		this.players = players;
		this.roundScores = roundScores;
	}
	
	public String[] getPlayers() {
		return players;
	}
	public void setPlayers(String[] players) {
		this.players = players;
	}
	
	public Integer[][] getRoundScores(){
		return roundScores;
	}
	public void setRoundScores(Integer[][] roundScores) {
		this.roundScores = roundScores;
	}
	
	public String toJson(){
		Gson g = new Gson();
		String output = g.toJson(this);
		return output;
	}
}
