package com.apptogo.runner.userdata;


public class UserData 
{
	public String key;
	
	//player
	public String playerName;
	public boolean ignoreContact;

	//powerup
	public String powerup;
	
	//coin
	public boolean collected;
	
	//obstacle
	public boolean active;
	public boolean playSound;
	
	//swamp
	public boolean touchSwamp;
	
	//hedgehog
	public boolean changeDirection;
	
	//all bodies
	public float bodyWidth;
	
	
	public UserData(Object key){
		this.key = key.toString();
		this.bodyWidth = -1.0f;
	}
	
	public UserData(Object key, String playerName){
		this.key = key.toString();
		this.playerName = playerName;
		this.bodyWidth = -1.0f;
	}
	
	public boolean isWidthNull()
	{
		if( this.bodyWidth == -1.0f )
			return true;
		return false;
	}
}
