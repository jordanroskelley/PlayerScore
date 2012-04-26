package org.theroskelleys.player_score;

import com.google.gson.Gson;

public class GameState
{
	private String[] players;
	private Integer[][] roundScores;
	private Boolean isTotalShowing;
	private String name;
	
	public GameState(String[] players, Integer[][] roundScores, Boolean isTotalShowing, String name){
		this.players = players;
		this.roundScores = roundScores;
		this.isTotalShowing = isTotalShowing;
		this.name = name;
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
	
	public Boolean getIsTotalShowing(){
		return isTotalShowing;
	}
	
	public void setIsTotalShowing(Boolean isTotalShowing){
		this.isTotalShowing = isTotalShowing;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String toJson(){
		Gson g = new Gson();
		String output = g.toJson(this);
		return output;
	}
}
