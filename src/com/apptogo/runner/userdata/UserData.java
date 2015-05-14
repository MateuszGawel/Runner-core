package com.apptogo.runner.userdata;

import com.badlogic.gdx.math.Vector2;


public class UserData 
{
	public String key;
	
	//player
	public String playerName;
	public boolean ignoreContact;
	public Vector2 previousPosition;
	
	//powerup
	public String powerup;
	public int abilityLevel;
	
	//coin
	public boolean collected;
	
	//obstacle
	public boolean active;
	public boolean alive;
	public boolean playSound;
	public boolean killingBottom;
	public boolean killingTop;
	public boolean killingDismemberment;
	
	//swamp
	public boolean touchSwamp;
	
	//hedgehog
	public boolean changeDirection;
	
	//all bodies
	public float bodyWidth;
	public float bodyHeight;
	
	
	public UserData(Object key){
		this.key = key.toString();
		this.bodyWidth = -1.0f;
	}
	
	public UserData(Object key, String playerName){
		this.key = key.toString();
		this.playerName = playerName;
		this.bodyWidth = -1.0f;
		this.bodyHeight = -1.0f;
	}
	
	public boolean isWidthNull()
	{
		if( this.bodyWidth == -1.0f )
			return true;
		return false;
	}
	
	public boolean isHeightNull()
	{
		if( this.bodyHeight == -1.0f )
			return true;
		return false;
	}
}
