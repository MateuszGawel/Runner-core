package com.apptogo.runner.userdata;

import com.apptogo.runner.logger.Logger;

public class UserData 
{
	public String key;
	
	//player
	public boolean me;
	public String playerName;
	public float slowPercent = 0; //od 0 do 1
	public boolean dieBottom;
	public boolean dieTop;
	public float speedBeforeLand;

	//powerup
	public String powerup;
	
	//obstacle
	public boolean active;
	
	
	
	public UserData(Object key){
		this.key = key.toString();
	}
}
