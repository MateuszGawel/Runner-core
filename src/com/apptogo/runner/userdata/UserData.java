package com.apptogo.runner.userdata;


public class UserData 
{
	public String key;
	
	//player
	public String playerName;
	public boolean ignoreContact;

	//powerup
	public String powerup;
	
	//obstacle
	public boolean active;
	public boolean playSound;
	
	//swamp
	public boolean touchSwamp;
	
	//hedgehog
	public boolean changeDirection;
	
	
	public UserData(Object key){
		this.key = key.toString();
	}
	
	public UserData(Object key, String playerName){
		this.key = key.toString();
		this.playerName = playerName;
	}
}
